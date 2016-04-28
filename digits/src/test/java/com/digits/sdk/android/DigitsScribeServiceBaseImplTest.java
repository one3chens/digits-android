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

import com.digits.sdk.android.DigitsScribeConstants.Action;
import com.digits.sdk.android.DigitsScribeConstants.Component;
import com.digits.sdk.android.DigitsScribeConstants.Element;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DigitsScribeServiceBaseImplTest {
    private TestScribeService service;
    @Mock
    private DigitsScribeClient client;
    @Captor
    private ArgumentCaptor<EventNamespace> eventNamespaceArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new TestScribeService(client);
    }

    @Test
    public void testImpression() throws Exception {
        service.impression();
        verify(client).scribe(eventNamespaceArgumentCaptor.capture());
        final EventNamespace actualEventNamespace = eventNamespaceArgumentCaptor.getValue();
        final EventNamespace expectedEventNamespace = createImpression();
        assertEquals(expectedEventNamespace, actualEventNamespace);
    }

    @Test
    public void testSuccess() throws Exception {
        service.success();
        verify(client).scribe(eventNamespaceArgumentCaptor.capture());
        final EventNamespace actualEventNamespace = eventNamespaceArgumentCaptor.getValue();
        final EventNamespace expectedEventNamespace = createSuccess();
        assertEquals(expectedEventNamespace, actualEventNamespace);
    }

    @Test
    public void testClick() throws Exception {
        service.click(Element.SUBMIT);
        verify(client).scribe(eventNamespaceArgumentCaptor.capture());
        final EventNamespace actualEventNamespace = eventNamespaceArgumentCaptor.getValue();
        final EventNamespace expectedEventNamespace = createClick();
        assertEquals(expectedEventNamespace, actualEventNamespace);
    }

    @Test
    public void testFailure() throws Exception {
        service.failure();
        verify(client).scribe(eventNamespaceArgumentCaptor.capture());
        final EventNamespace actualEventNamespace = eventNamespaceArgumentCaptor.getValue();
        final EventNamespace expectedEventNamespace = createFailure();
        assertEquals(expectedEventNamespace, actualEventNamespace);
    }

    @Test
    public void testError() throws Exception {
        service.error(TestConstants.ANY_EXCEPTION);
        verify(client).scribe(eventNamespaceArgumentCaptor.capture());
        final EventNamespace actualEventNamespace = eventNamespaceArgumentCaptor.getValue();
        final EventNamespace expectedEventNamespace = createException();
        assertEquals(expectedEventNamespace, actualEventNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullScribeClient() throws Exception {
        new TestScribeService(null);
    }

    private EventNamespace createException() {
        return DigitsScribeServiceBaseImpl.DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.ERROR.getAction())
                .builder();
    }

    private EventNamespace createImpression() {
        return DigitsScribeServiceBaseImpl.DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.IMPRESSION.getAction())
                .builder();
    }

    private EventNamespace createSuccess() {
        return DigitsScribeServiceBaseImpl.DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.SUCCESS.getAction())
                .builder();
    }

    private EventNamespace createFailure() {
        return DigitsScribeServiceBaseImpl.DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.FAILURE.getAction())
                .builder();
    }

    private EventNamespace createClick() {
        return DigitsScribeServiceBaseImpl.DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(Element.SUBMIT.getElement())
                .setAction(Action.CLICK.getAction())
                .builder();
    }

    class TestScribeService extends DigitsScribeServiceBaseImpl {
        TestScribeService(DigitsScribeClient scribeClient) {
            super(scribeClient, Component.EMPTY);
        }
    }
}
