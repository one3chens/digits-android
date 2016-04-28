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

public class DigitsScribeServiceBaseImpl implements DigitsScribeService {
    static final String SCRIBE_CLIENT = "tfw";
    static final String SCRIBE_PAGE = "android";
    static final String SCRIBE_SECTION = "digits";
    static final EventNamespace.Builder DIGITS_EVENT_BUILDER = new EventNamespace.Builder()
            .setClient(SCRIBE_CLIENT)
            .setPage(SCRIBE_PAGE)
            .setSection(SCRIBE_SECTION);

    private final Component component;
    private final DigitsScribeClient scribeClient;

    public DigitsScribeServiceBaseImpl(DigitsScribeClient scribeClient, Component component){
        if (component == null) {
            throw new NullPointerException("component must not be null");
        }

        if (scribeClient == null) {
            throw new NullPointerException("scribe client must not be null");
        }

        this.scribeClient = scribeClient;
        this.component = component;
    }

    @Override
    public void impression() {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(component.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.IMPRESSION.getAction())
                .builder();
        scribeClient.scribe(ns);
    }

    @Override
    public void failure() {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(component.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.FAILURE.getAction())
                .builder();
        scribeClient.scribe(ns);
    }

    @Override
    public void click(Element element) {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(component.getComponent())
                .setElement(element.getElement())
                .setAction(Action.CLICK.getAction())
                .builder();
        scribeClient.scribe(ns);
    }

    @Override
    public void success() {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(component.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.SUCCESS.getAction())
                .builder();
        scribeClient.scribe(ns);
    }

    @Override
    public void error(DigitsException exception) {
        final EventNamespace ns = DIGITS_EVENT_BUILDER
                .setComponent(component.getComponent())
                .setElement(Element.EMPTY.getElement())
                .setAction(Action.ERROR.getAction())
                .builder();
        scribeClient.scribe(ns);
    }
}
