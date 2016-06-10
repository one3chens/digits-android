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
package com.example.app.digits;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import com.digits.sdk.android.DigitsEventLogger;
import com.digits.sdk.android.DigitsEventDetails;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * This logger does multiple things to demonstrate the flexibility
 * we intend to provide
 * 1) Generate Answers Custom Events
 * 2) Log to stdOut
 */
public class AnswersLogger extends DigitsEventLogger {
    private final String TAG = "AnswersLogger";

    @Override
    public void loginBegin(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "loginBegin")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));

        Fabric.getLogger().d(TAG, "loginBegin event received");
        Fabric.getLogger().d(TAG, String.format(Locale.US, "timeElapsed = %d%n",
                details.elapsedTimeInMillis / 1000));
        Fabric.getLogger().d(TAG, String.format(Locale.US, "language = %s",
                details.language));
    }

    @Override
    public void phoneNumberImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));

        Fabric.getLogger().d(TAG, "phoneNumberImpression event received");
        Fabric.getLogger().d(TAG, String.format(Locale.US, "timeElapsed = %d%n",
                details.elapsedTimeInMillis / 1000));
        Fabric.getLogger().d(TAG, String.format(Locale.US, "language = %s",
                details.language));
    }

    @Override
    public void phoneNumberSubmit(DigitsEventDetails details) {
        statAndPrintAll("phoneNumberSubmit", details);
    }

    @Override
    public void phoneNumberSuccess(DigitsEventDetails details) {
        statAndPrintAll("phoneNumberSuccess", details);
    }

    @Override
    public void confirmationCodeImpression(DigitsEventDetails details) {
        statAndPrintAll("confirmationCodeImpression", details);
    }

    @Override
    public void confirmationCodeSubmit(DigitsEventDetails details) {
        statAndPrintAll("confirmationCodeSubmit", details);
    }

    @Override
    public void confirmationCodeSuccess(DigitsEventDetails details) {
        statAndPrintAll("confirmationCodeSuccess", details);
    }

    @Override
    public void twoFactorPinImpression(DigitsEventDetails details) {
        statAndPrintAll("twoFactorPinImpression", details);
    }

    @Override
    public void twoFactorPinSubmit(DigitsEventDetails details) {
        statAndPrintAll("twoFactorPinSubmit", details);
    }

    @Override
    public void twoFactorPinSuccess(DigitsEventDetails details) {
        statAndPrintAll("twoFactorPinSuccess", details);
    }

    @Override
    public void emailImpression(DigitsEventDetails details) {
        statAndPrintAll("emailImpression", details);
    }

    @Override
    public void emailSubmit(DigitsEventDetails details) {
        statAndPrintAll("emailSubmit", details);
    }

    @Override
    public void emailSuccess(DigitsEventDetails details) {
        statAndPrintAll("emailSuccess", details);
    }

    @Override
    public void failureImpression(DigitsEventDetails details) {
        statAndPrintLanguageAndElapsedTime("failureImpression", details);
    }

    @Override
    public void failureRetryClick(DigitsEventDetails details) {
        statAndPrintLanguageAndElapsedTime("failureRetryClick", details);
    }

    @Override
    public void failureDismissClick(DigitsEventDetails details) {
        statAndPrintLanguageAndElapsedTime("failureDismissClick", details);
    }

    @Override
    public void loginSuccess(DigitsEventDetails details) {
        statAndPrintAll("loginSuccess", details);
    }

    @Override
    public void loginFailure(DigitsEventDetails details) {
        statAndPrintLanguageAndElapsedTime("loginFailure", details);
    }

    private void statAndPrintAll(String event, DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent(event)
                .putCustomAttribute("Action", String.format("%s event received", event))
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));

        Fabric.getLogger().d(TAG, String.format(Locale.US, "%s event received", event));
        Fabric.getLogger().d(TAG, details.toString());
    }

    private void statAndPrintLanguageAndElapsedTime(String event, DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent(event)
                .putCustomAttribute("Action", String.format("%s event received", event))
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));

        Fabric.getLogger().d(TAG, String.format(Locale.US, "%s event received", event));
        Fabric.getLogger().d(TAG, details.toString());
    }
}
