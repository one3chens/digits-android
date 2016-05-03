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

public class DigitsScribeServiceBaseImpl implements DigitsScribeService {
    private final Component component;
    private final DigitsScribeClient digitsScribeClient;

    public DigitsScribeServiceBaseImpl(DigitsScribeClient digitsScribeClient, Component component){
        if (component == null) {
            throw new IllegalArgumentException("component must not be null");
        }

        if (digitsScribeClient == null) {
            throw new IllegalArgumentException("digits scribe client must not be null");
        }

        this.digitsScribeClient = digitsScribeClient;
        this.component = component;
    }

    @Override
    public void impression() {
        digitsScribeClient.impression(component);
    }

    @Override
    public void failure() {
        digitsScribeClient.failure(component);
    }

    @Override
    public void click(Element element) {
        digitsScribeClient.click(component, element);
    }

    @Override
    public void success() {
        digitsScribeClient.success(component);
    }

    @Override
    public void error(DigitsException exception) {
        digitsScribeClient.error(component, exception);
    }
}
