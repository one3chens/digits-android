/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.digits.sdk.android;

import android.content.Intent;
import android.database.Cursor;

import com.google.gson.Gson;

import io.fabric.sdk.android.Logger;
import io.fabric.sdk.android.services.concurrency.internal.RetryThreadPoolExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ContactsUploadServiceTests {
    private Cursor cursor;
    private ContactsHelper helper;
    private RetryThreadPoolExecutor executor;
    private ContactsClient contactsClient;
    private ContactsPreferenceManager perfManager;
    private ArrayList<String> cradList;
    private ContactsUploadService service;
    private Logger logger;
    private ArgumentCaptor<Intent> intentCaptor;

    @Before
    public void setUp() throws Exception {
        executor = mock(RetryThreadPoolExecutor.class);
        perfManager = mock(MockContactsPreferenceManager.class);
        contactsClient = mock(ContactsClient.class);
        logger = mock(Logger.class);
        cursor = ContactsHelperTests.createCursor();
        cradList = ContactsHelperTests.createCardList();
        intentCaptor = ArgumentCaptor.forClass(Intent.class);
        helper = mock(ContactsHelper.class);
        when(helper.getContactsCursor()).thenReturn(cursor);
        when(helper.createContactList(cursor)).thenReturn(cradList);

        service = spy(new ContactsUploadService(contactsClient, helper, perfManager, executor,
                logger, Locale.JAPANESE));
    }

    @Test
    public void testOnHandleIntent() throws Exception {
        when(executor.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Runnable) invocationOnMock.getArguments()[0]).run();
                return null;
            }
        }).when(executor).scheduleWithRetry(any(Runnable.class));

        service.onHandleIntent(null);

        verify(helper).getContactsCursor();
        verify(helper).createContactList(cursor);
        verify(executor).scheduleWithRetry(any(Runnable.class));
        verify(executor).shutdown();
        verify(executor).awaitTermination(anyLong(), any(TimeUnit.class));

        verify(service).sendBroadcast(intentCaptor.capture());
        assertEquals(ContactsUploadService.UPLOAD_COMPLETE, intentCaptor.getValue().getAction());

        verify(perfManager).setContactImportPermissionGranted();
        verify(perfManager).setContactsUploaded(cradList.size());
        verify(perfManager).setContactsReadTimestamp(anyLong());

        final ContactsUploadResult result = intentCaptor.getValue()
                .getParcelableExtra(ContactsUploadService.UPLOAD_COMPLETE_EXTRA);
        assertEquals(cradList.size(), result.successCount);
        assertEquals(cradList.size(), result.totalCount);
    }

    @Test
    public void testLoggingSuccess() throws Exception {
        when(executor.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Runnable) invocationOnMock.getArguments()[0]).run();
                return null;
            }
        }).when(executor).scheduleWithRetry(any(Runnable.class));
        final RetrofitError retrofitError = mock(RetrofitError.class);
        final List<UploadError> errors = new ArrayList<>();
        final int errorCode = 88;
        final int httpStatus = 401;
        final String errorMessage = "Rate limit";
        errors.add(new UploadError(errorCode, errorMessage, 1));
        final Response response = createResponse(httpStatus, toJson(new UploadResponse(errors)));
        when(retrofitError.getResponse()).thenReturn(response);
        when(retrofitError.getStackTrace()).thenReturn(new StackTraceElement[0]);
        when(contactsClient.uploadContacts(any(Vcards.class))).thenThrow(retrofitError);

        service.onHandleIntent(null);

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).e(eq(Digits.TAG), captor.capture());
        assertEquals(captor.getValue(), String.format(Locale.JAPANESE,
                ContactsUploadService.ERROR_LOG_FORMAT, httpStatus, errorCode, errorMessage));
    }

    @Test
    public void testLoggingNullApiError() throws Exception {
        when(executor.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Runnable) invocationOnMock.getArguments()[0]).run();
                return null;
            }
        }).when(executor).scheduleWithRetry(any(Runnable.class));
        final RetrofitError retrofitError = mock(RetrofitError.class);
        final int httpStatus = 401;
        final Response response = createResponse(httpStatus, "{}");
        when(retrofitError.getResponse()).thenReturn(response);
        when(retrofitError.getStackTrace()).thenReturn(new StackTraceElement[0]);
        when(contactsClient.uploadContacts(any(Vcards.class))).thenThrow(retrofitError);

        service.onHandleIntent(null);

        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).e(eq(Digits.TAG), captor.capture());
        assertEquals(captor.getValue(), String.format(Locale.JAPANESE,
                ContactsUploadService.ERROR_LOG_FORMAT, httpStatus, 0, null));
    }

    @Test
    public void testOnHandleIntent_uploadTimeout() throws Exception {
        when(executor.awaitTermination(anyLong(), any(TimeUnit.class))).thenReturn(false);

        service.onHandleIntent(null);

        verify(helper).getContactsCursor();
        verify(helper).createContactList(cursor);
        verify(executor).scheduleWithRetry(any(Runnable.class));
        verify(executor).shutdown();
        verify(executor).awaitTermination(anyLong(), any(TimeUnit.class));
        verify(executor).shutdownNow();

        verify(service).sendBroadcast(intentCaptor.capture());
        assertEquals(ContactsUploadService.UPLOAD_FAILED, intentCaptor.getValue().getAction());

        verify(perfManager).setContactImportPermissionGranted();
        verifyNoMoreInteractions(perfManager);
    }

    @Test
    public void testGetNumberOfPages() {
        assertEquals(1, service.getNumberOfPages(100));
        assertEquals(1, service.getNumberOfPages(50));
        assertEquals(2, service.getNumberOfPages(101));
        assertEquals(2, service.getNumberOfPages(199));
    }

    @Test
    public void testSendFailureBroadcast() {
        service.sendFailureBroadcast();

        verify(service).sendBroadcast(intentCaptor.capture());
        assertEquals(ContactsUploadService.UPLOAD_FAILED, intentCaptor.getValue().getAction());
    }

    @Test
    public void testSendSuccessBroadcast() {
        service.sendSuccessBroadcast(new ContactsUploadResult(1, 1));

        verify(service).sendBroadcast(intentCaptor.capture());
        assertEquals(ContactsUploadService.UPLOAD_COMPLETE, intentCaptor.getValue().getAction());
        final ContactsUploadResult result = intentCaptor.getValue()
                .getParcelableExtra(ContactsUploadService.UPLOAD_COMPLETE_EXTRA);
        assertEquals(1, result.successCount);
        assertEquals(1, result.totalCount);
    }

    // Response is final, which isn't mockable by Mockito, so this fn creates a stub.
    Response createResponse(int status, String body) throws UnsupportedEncodingException {
        return new Response("url", status, "reason", Collections.<Header>emptyList(),
                new TypedByteArray("application/json", body.getBytes("UTF-8")));
    }

    String toJson(UploadResponse response) {
        return new Gson().toJson(response);
    }
}
