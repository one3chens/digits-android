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

class DigitsEventCollector {
    private final DigitsScribeClient digitsScribeClient;
    private DigitsEventLogger externalEventLogger;

    DigitsEventCollector(DigitsScribeClient digitsScribeClient){
        if (digitsScribeClient == null) {
            throw new IllegalArgumentException("digits scribe client must not be null");
        }

        this.digitsScribeClient = digitsScribeClient;
    }

    void setLoggerResultReceiver(DigitsEventLogger externalEventLogger){
        this.externalEventLogger = externalEventLogger;
    }

    //Auth/External API events
    public void authImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.EMPTY);
        if (externalEventLogger != null) externalEventLogger.loginBegin(details);
    }

    public void authSuccess() {
        digitsScribeClient.loginSuccess();
        if (externalEventLogger != null) externalEventLogger.loginSuccess();
    }

    public void authFailure() {
        digitsScribeClient.failure(Component.EMPTY);
    }

    //Phone screen events
    public void phoneScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.AUTH);
        if (externalEventLogger != null)
            externalEventLogger.phoneNumberImpression(details);
    }
    public void countryCodeClickOnPhoneScreen() {
        digitsScribeClient.click(Component.AUTH, Element.COUNTRY_CODE);
    }

    public void submitClickOnPhoneScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.AUTH, Element.SUBMIT);
        if (externalEventLogger != null) externalEventLogger.phoneNumberSubmit(details);
    }

    public void retryClickOnPhoneScreen() {
        digitsScribeClient.click(Component.AUTH, Element.RETRY);
    }

    public void submitPhoneSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.AUTH);
        if (externalEventLogger != null) externalEventLogger.phoneNumberSuccess(details);
    }

    public void submitPhoneFailure() {
        digitsScribeClient.failure(Component.AUTH);
    }

    public void submitPhoneException(DigitsException exception) {
        digitsScribeClient.error(Component.AUTH, exception);
    }

    //Login screen events
    public void loginScreenImpression() {
        digitsScribeClient.impression(Component.LOGIN);
    }

    public void submitClickOnLoginScreen() {
        digitsScribeClient.click(Component.LOGIN, Element.SUBMIT);
    }

    public void resendClickOnLoginScreen() {
        digitsScribeClient.click(Component.LOGIN, Element.RESEND);
    }

    public void callMeClickOnLoginScreen() {
        digitsScribeClient.click(Component.LOGIN, Element.CALL);
    }

    public void loginCodeSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.LOGIN);
        if (externalEventLogger != null) externalEventLogger.loginSuccess();

    }

    public void loginFailure() {
        digitsScribeClient.failure(Component.LOGIN);
    }

    public void loginException(DigitsException exception) {
        digitsScribeClient.error(Component.LOGIN, exception);
    }

    //Signup screen events
    public void signupScreenImpression() {
        digitsScribeClient.impression(Component.SIGNUP);
    }

    public void submitClickOnSignupScreen() {
        digitsScribeClient.click(Component.SIGNUP, Element.SUBMIT);
    }

    public void resendClickOnSignupScreen() {
        digitsScribeClient.click(Component.SIGNUP, Element.RESEND);
    }

    public void callMeClickOnSignupScreen() {
        digitsScribeClient.click(Component.SIGNUP, Element.CALL);
    }

    public void signupSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.SIGNUP);
        if (externalEventLogger != null) externalEventLogger.loginSuccess();
    }

    public void signupFailure() {
        digitsScribeClient.failure(Component.SIGNUP);
    }

    public void signupException(DigitsException exception) {
        digitsScribeClient.error(Component.SIGNUP, exception);
    }

    //Pin screen events
    public void pinScreenImpression() {
        digitsScribeClient.impression(Component.PIN);
    }

    public void submitClickOnPinScreen() {
        digitsScribeClient.click(Component.PIN, Element.SUBMIT);
    }

    public void twoFactorPinVerificationSuccess() {
        digitsScribeClient.success(Component.PIN);
    }

    public void twoFactorPinVerificationFailure() {
        digitsScribeClient.failure(Component.PIN);
    }

    public void twoFactorPinVerificationException(DigitsException exception) {
        digitsScribeClient.error(Component.PIN, exception);
    }

    //Email screen events
    public void emailScreenImpression() {
        digitsScribeClient.impression(Component.EMAIL);
    }

    public void submitClickOnEmailScreen() {
        digitsScribeClient.click(Component.EMAIL, Element.SUBMIT);
    }

    public void submitEmailSuccess() {
        digitsScribeClient.success(Component.EMAIL);
    }

    public void submitEmailFailure() {
        digitsScribeClient.failure(Component.EMAIL);
    }

    public void submitEmailException(DigitsException exception) {
        digitsScribeClient.error(Component.EMAIL, exception);
    }
    //Contacts upload screen events
    public void contactScreenImpression() {
        digitsScribeClient.impression(Component.CONTACTS);
    }

    public void cancelClickOnContactScreen() {
        digitsScribeClient.click(Component.CONTACTS, Element.CANCEL);
    }

    public void submitClickOnContactScreen() {
        digitsScribeClient.click(Component.CONTACTS, Element.SUBMIT);
    }

    //Failure screen events
    public void failureScreenImpression() {
        digitsScribeClient.impression(Component.FAILURE);
    }

    public void retryClickOnFailureScreen() {
        digitsScribeClient.click(Component.FAILURE, Element.RETRY);
    }

    public void dismissClickOnFailureScreen() {
        digitsScribeClient.click(Component.FAILURE, Element.DISMISS);
    }
}
