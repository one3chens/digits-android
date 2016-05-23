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

import java.util.HashSet;
import java.util.Set;

class DigitsEventCollector {
    private final DigitsScribeClient digitsScribeClient;
    private final Set<DigitsEventLogger> eventLoggers;

    DigitsEventCollector(DigitsScribeClient digitsScribeClient, DigitsEventLogger... loggers){
        if (digitsScribeClient == null) {
            throw new IllegalArgumentException("digits scribe client must not be null");
        }

        this.digitsScribeClient = digitsScribeClient;
        eventLoggers = new HashSet<>();

        for (DigitsEventLogger logger: loggers) {
            eventLoggers.add(logger);
        }
    }

    void addDigitsEventLogger(DigitsEventLogger eventLogger){
        this.eventLoggers.add(eventLogger);
    }

    //Auth/External API events
    public void authImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.EMPTY);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.loginBegin(details);
        }
    }

    public void authSuccess(DigitsEventDetails details) {
        digitsScribeClient.loginSuccess();
        for (DigitsEventLogger logger: eventLoggers) {
            logger.loginSuccess(details);
        }
    }

    public void authFailure(DigitsEventDetails details) {
        digitsScribeClient.failure(Component.EMPTY);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.loginFailure(details);
        }
    }

    //Phone screen events
    public void phoneScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.AUTH);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.phoneNumberImpression(details);
        }
    }
    public void countryCodeClickOnPhoneScreen() {
        digitsScribeClient.click(Component.AUTH, Element.COUNTRY_CODE);
    }

    public void submitClickOnPhoneScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.AUTH, Element.SUBMIT);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.phoneNumberSubmit(details);
        }
    }

    public void retryClickOnPhoneScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.AUTH, Element.RETRY);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.phoneNumberSubmit(details);
        }
    }

    public void submitPhoneSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.AUTH);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.phoneNumberSuccess(details);
        }
    }

    public void submitPhoneFailure() {
        digitsScribeClient.failure(Component.AUTH);
    }

    public void submitPhoneException(DigitsException exception) {
        digitsScribeClient.error(Component.AUTH, exception);
    }

    //Login screen events
    public void loginScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.LOGIN);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeImpression(details);
        }
    }

    public void submitClickOnLoginScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.LOGIN, Element.SUBMIT);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeSubmit(details);
        }
    }

    public void resendClickOnLoginScreen() {
        digitsScribeClient.click(Component.LOGIN, Element.RESEND);
    }

    public void callMeClickOnLoginScreen() {
        digitsScribeClient.click(Component.LOGIN, Element.CALL);
    }

    public void loginCodeSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.LOGIN);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeSuccess(details);
        }
    }

    public void loginFailure() {
        digitsScribeClient.failure(Component.LOGIN);
    }

    public void loginException(DigitsException exception) {
        digitsScribeClient.error(Component.LOGIN, exception);
    }

    //Signup screen events
    public void signupScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.SIGNUP);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeImpression(details);
        }
    }

    public void submitClickOnSignupScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.SIGNUP, Element.SUBMIT);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeSubmit(details);
        }
    }

    public void resendClickOnSignupScreen() {
        digitsScribeClient.click(Component.SIGNUP, Element.RESEND);
    }

    public void callMeClickOnSignupScreen() {
        digitsScribeClient.click(Component.SIGNUP, Element.CALL);
    }

    public void signupSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.SIGNUP);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.confirmationCodeSuccess(details);
        }
    }

    public void signupFailure() {
        digitsScribeClient.failure(Component.SIGNUP);
    }

    public void signupException(DigitsException exception) {
        digitsScribeClient.error(Component.SIGNUP, exception);
    }

    //Pin screen events
    public void pinScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.PIN);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.twoFactorPinImpression(details);
        }
    }

    public void submitClickOnPinScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.PIN, Element.SUBMIT);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.twoFactorPinSubmit(details);
        }
    }

    public void twoFactorPinVerificationSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.PIN);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.twoFactorPinSuccess(details);
        }
    }

    public void twoFactorPinVerificationFailure() {
        digitsScribeClient.failure(Component.PIN);
    }

    public void twoFactorPinVerificationException(DigitsException exception) {
        digitsScribeClient.error(Component.PIN, exception);
    }

    //Email screen events
    public void emailScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.EMAIL);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.emailImpression(details);
        }
    }

    public void submitClickOnEmailScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.EMAIL, Element.SUBMIT);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.emailSubmit(details);
        }
    }

    public void submitEmailSuccess(DigitsEventDetails details) {
        digitsScribeClient.success(Component.EMAIL);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.emailSuccess(details);
        }
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
    public void failureScreenImpression(DigitsEventDetails details) {
        digitsScribeClient.impression(Component.FAILURE);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.failureImpression(details);
        }
    }

    public void retryClickOnFailureScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.FAILURE, Element.RETRY);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.failureRetryClick(details);
        }
    }

    public void dismissClickOnFailureScreen(DigitsEventDetails details) {
        digitsScribeClient.click(Component.FAILURE, Element.DISMISS);
        for (DigitsEventLogger logger: eventLoggers) {
            logger.failureDismissClick(details);
        }
    }
}
