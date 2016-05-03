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

import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DigitsScribeServiceBaseImplTest {
    @Mock
    private DigitsScribeClient client;
    @Captor
    private ArgumentCaptor<EventNamespace> eventNamespaceArgumentCaptor;
    private Component component = Component.AUTH;
    private DigitsScribeService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new DigitsScribeServiceBaseImpl(client, component);
    }

    @Test
    public void testImpression() throws Exception {
        service.impression();
        verify(client).impression(component);
    }

    @Test
    public void testSuccess() throws Exception {
        service.success();
        verify(client).success(component);
    }

    @Test
    public void testClick() throws Exception {
        service.click(Element.SUBMIT);
        verify(client).click(component, Element.SUBMIT);
    }

    @Test
    public void testFailure() throws Exception {
        service.failure();
        verify(client).failure(component);
    }

    @Test
    public void testError() throws Exception {
        service.error(TestConstants.ANY_EXCEPTION);
        verify(client).error(component, TestConstants.ANY_EXCEPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullScribeClient() throws Exception {
        new DigitsScribeServiceBaseImpl(null, Component.AUTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullComponent() throws Exception {
        new DigitsScribeServiceBaseImpl(client, null);
    }
}
