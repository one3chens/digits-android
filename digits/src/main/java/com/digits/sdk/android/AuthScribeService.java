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

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.digits.sdk.android.DigitsScribeConstants.Component;

class AuthScribeService extends DigitsScribeServiceBaseImpl {
    static final String LOGGED_IN_ACTION = "logged_in";
    private final DigitsScribeClient scribeClient;

    AuthScribeService(DigitsScribeClient digitsScribeClient) {
        super(Digits.getInstance().getScribeClient(), Component.EMPTY);
        this.scribeClient = digitsScribeClient;
    }

    @Override
    public void success() {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(Component.EMPTY.getComponent())
                .setElement(DigitsScribeConstants.Element.EMPTY.getElement())
                .setAction(LOGGED_IN_ACTION)
                .builder();
        this.scribeClient.scribe(ns);
    }
}
