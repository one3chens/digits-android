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

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.SpannedString;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginCodeActivityDelegateTests extends
        DigitsActivityDelegateTests<LoginCodeActivityDelegate> {
    @Override
    public LoginCodeActivityDelegate getDelegate() {
        return spy(new DummyLoginCodeActivityDelegate(scribeService));
    }

    public void testIsValid() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertTrue(delegate.isValid(bundle));
    }

    public void testIsValid_missingResultReceiver() {
        final Bundle bundle = new Bundle();
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingPhoneNumber() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingRequestId() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_USER_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingUserId() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, "");
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, "");

        assertFalse(delegate.isValid(bundle));
    }

    public void testGetLayoutId() {
        assertEquals(R.layout.dgts__activity_confirmation, delegate.getLayoutId());
    }

    @Override
    public void testSetUpTermsText() throws Exception {
        delegate.config = new AuthConfig();
        delegate.config.tosUpdate = Boolean.FALSE;
        delegate.tosFormatHelper = tosFormatHelper;
        doReturn(new SpannedString("")).when(tosFormatHelper).getFormattedTerms(anyInt());
        super.testSetUpTermsText();
        verify(tosFormatHelper).getFormattedTerms(R.string.dgts__terms_text_sign_in);
        verify(textView).setText(new SpannedString(""));
    }

    public void testSetUpTermsText_tosUpdated() throws Exception {
        delegate.tosFormatHelper = tosFormatHelper;
        doReturn(new SpannedString("")).when(tosFormatHelper)
                .getFormattedTerms(anyInt());
        delegate.config = new AuthConfig();
        delegate.config.tosUpdate = Boolean.TRUE;
        super.testSetUpTermsText();
        verify(tosFormatHelper).getFormattedTerms(eq(R.string.dgts__terms_text_updated));
        verify(textView).setText(new SpannedString(""));
    }

    @Override
    public void testSetUpSendButton() throws Exception {
        super.testSetUpSendButton();
        verify(button).setStatesText(R.string.dgts__continue, R.string.dgts__sending,
                R.string.dgts__done);
        verify(button).showStart();
    }

    public void testOnResume() {
        delegate.controller = controller;
        delegate.onResume();
        verify(controller).onResume();
        verify(scribeService).impression();
    }

    public void testSetUpSmsIntercept_permissionDenied() {
        when(activity.checkCallingOrSelfPermission("android.permission.RECEIVE_SMS"))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        delegate.setUpSmsIntercept(activity, editText);

        verify(activity).checkCallingOrSelfPermission("android.permission.RECEIVE_SMS");
        verifyNoMoreInteractions(activity);
    }

    public void testSetUpSmsIntercept_permissionGranted() {
        when(activity.checkCallingOrSelfPermission("android.permission.RECEIVE_SMS"))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        delegate.setUpSmsIntercept(activity, editText);

        verify(activity).checkCallingOrSelfPermission("android.permission.RECEIVE_SMS");
        verify(activity).registerReceiver(any(SmsBroadcastReceiver.class), any(IntentFilter.class));
    }

    public class DummyLoginCodeActivityDelegate extends LoginCodeActivityDelegate {

        DummyLoginCodeActivityDelegate(DigitsScribeService scribeService) {
            super(scribeService);
        }
    }
}
