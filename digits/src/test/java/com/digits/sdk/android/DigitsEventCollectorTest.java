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
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DigitsEventCollectorTest {
    @Captor
    private ArgumentCaptor<EventNamespace> eventNamespaceArgumentCaptor;
    private final DigitsScribeClient digitsScribeClient = mock(DigitsScribeClient.class);
    private final FailFastEventDetailsChecker failFastEventDetailsChecker =
            mock(FailFastEventDetailsChecker.class);
    private DigitsEventCollector digitsEventCollector =
            new DigitsEventCollector(digitsScribeClient, failFastEventDetailsChecker);
    private final DigitsException exception = new DigitsException("exception");
    private final DigitsEventDetails details = new DigitsEventDetailsBuilder()
            .withAuthStartTime(System.currentTimeMillis())
            .withCurrentTime(System.currentTimeMillis())
            .build();
    private final DigitsEventLogger digitsEventLogger2 = mock(DigitsEventLogger.class);
    private final DigitsEventLogger digitsEventLogger1 = mock(DigitsEventLogger.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        digitsEventCollector = new DigitsEventCollector(digitsScribeClient,
                failFastEventDetailsChecker, digitsEventLogger1);
    }

    @Test
    public void testAuthImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.authImpression(details);
        verify(failFastEventDetailsChecker).loginBegin(details);
        verify(digitsScribeClient).impression(Component.EMPTY);
        verify(digitsEventLogger1).loginBegin(details);
        verify(digitsEventLogger2).loginBegin(details);
    }

    @Test
    public void testAuthSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.authSuccess(details);
        verify(failFastEventDetailsChecker).loginSuccess(details);
        verify(digitsScribeClient).loginSuccess();
        verify(digitsEventLogger1).loginSuccess(details);
        verify(digitsEventLogger2).loginSuccess(details);
    }

    @Test
    public void testAuthFailure() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.authFailure(details);
        verify(failFastEventDetailsChecker).loginFailure(details);
        verify(digitsScribeClient).failure(Component.EMPTY);
        verify(digitsEventLogger1).loginFailure(details);
        verify(digitsEventLogger2).loginFailure(details);
    }

    //Phone screen events
    @Test
    public void testPhoneScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.phoneScreenImpression(details);
        verify(failFastEventDetailsChecker).phoneNumberImpression(details);
        verify(digitsScribeClient).impression(Component.AUTH);
        verify(digitsEventLogger1).phoneNumberImpression(details);
        verify(digitsEventLogger2).phoneNumberImpression(details);
    }

    @Test
    public void testCountryCodeClickOnPhoneScreen() {
        digitsEventCollector.countryCodeClickOnPhoneScreen();
        verify(digitsScribeClient).click(Component.AUTH,
                DigitsScribeConstants.Element.COUNTRY_CODE);
    }

    @Test
    public void testSubmitClickOnPhoneScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitClickOnPhoneScreen(details);
        verify(failFastEventDetailsChecker).phoneNumberSubmit(details);
        verify(digitsScribeClient).click(Component.AUTH, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger1).phoneNumberSubmit(details);
        verify(digitsEventLogger2).phoneNumberSubmit(details);
    }

    @Test
    public void testRetryClickOnPhoneScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.retryClickOnPhoneScreen(details);
        verify(failFastEventDetailsChecker).phoneNumberSubmit(details);
        verify(digitsScribeClient).click(Component.AUTH, DigitsScribeConstants.Element.RETRY);
        verify(digitsEventLogger1).phoneNumberSubmit(details);
        verify(digitsEventLogger2).phoneNumberSubmit(details); }

    @Test
    public void testSubmitPhoneSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitPhoneSuccess(details);
        verify(failFastEventDetailsChecker).phoneNumberSuccess(details);
        verify(digitsScribeClient).success(Component.AUTH);
        verify(digitsEventLogger1).phoneNumberSuccess(details);
        verify(digitsEventLogger2).phoneNumberSuccess(details);
    }

    @Test
    public void testSubmitPhoneFailure() {
        digitsEventCollector.submitPhoneFailure();
        verify(digitsScribeClient).failure(Component.AUTH);
    }

    @Test
    public void testSubmitPhoneException() {
        digitsEventCollector.submitPhoneException(exception);
        verify(digitsScribeClient).error(Component.AUTH, exception);
    }

    //Login screen events
    @Test
    public void testLoginScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.loginScreenImpression(details);
        verify(failFastEventDetailsChecker).confirmationCodeImpression(details);
        verify(digitsScribeClient).impression(Component.LOGIN);
        verify(digitsEventLogger1).confirmationCodeImpression(details);
        verify(digitsEventLogger2).confirmationCodeImpression(details);
    }

    @Test
    public void testSubmitClickOnLoginScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitClickOnLoginScreen(details);
        verify(failFastEventDetailsChecker).confirmationCodeSubmit(details);
        verify(digitsScribeClient).click(Component.LOGIN, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger1).confirmationCodeSubmit(details);
        verify(digitsEventLogger2).confirmationCodeSubmit(details);
    }

    @Test
    public void testResendClickOnLoginScreen() {
        digitsEventCollector.resendClickOnLoginScreen();
        verify(digitsScribeClient).click(Component.LOGIN, DigitsScribeConstants.Element.RESEND);
    }

    @Test
    public void testCallMeClickOnLoginScreen() {
        digitsEventCollector.callMeClickOnLoginScreen();
        verify(digitsScribeClient).click(Component.LOGIN, DigitsScribeConstants.Element.CALL);
    }

    @Test
    public void testLoginCodeSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.loginCodeSuccess(details);
        verify(failFastEventDetailsChecker).confirmationCodeSuccess(details);
        verify(digitsScribeClient).success(Component.LOGIN);
        verify(digitsEventLogger1).confirmationCodeSuccess(details);
        verify(digitsEventLogger2).confirmationCodeSuccess(details);
    }

    @Test
    public void testLoginFailure() {
        digitsEventCollector.loginFailure();
        verify(digitsScribeClient).failure(Component.LOGIN);
    }

    @Test
    public void testLoginException() {
        digitsEventCollector.loginException(exception);
        verify(digitsScribeClient).error(Component.LOGIN, exception);
    }

    //Signup screen events
    @Test
    public void testConfirmationScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.signupScreenImpression(details);
        verify(failFastEventDetailsChecker).confirmationCodeImpression(details);
        verify(digitsScribeClient).impression(Component.SIGNUP);
        verify(digitsEventLogger1).confirmationCodeImpression(details);
        verify(digitsEventLogger2).confirmationCodeImpression(details);
    }

    @Test
    public void testSubmitClickOnSignupScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitClickOnSignupScreen(details);
        verify(failFastEventDetailsChecker).confirmationCodeSubmit(details);
        verify(digitsScribeClient).click(Component.SIGNUP, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger1).confirmationCodeSubmit(details);
        verify(digitsEventLogger2).confirmationCodeSubmit(details);
    }

    @Test
    public void testResendClickOnSignupScreen() {
        digitsEventCollector.resendClickOnSignupScreen();
        verify(digitsScribeClient).click(Component.SIGNUP, DigitsScribeConstants.Element.RESEND);
    }

    @Test
    public void testCallMeClickOnSignupScreen() {
        digitsEventCollector.callMeClickOnSignupScreen();
        verify(digitsScribeClient).click(Component.SIGNUP, DigitsScribeConstants.Element.CALL);
    }

    @Test
    public void testSignupSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.signupSuccess(details);
        verify(failFastEventDetailsChecker).confirmationCodeSuccess(details);
        verify(digitsScribeClient).success(Component.SIGNUP);
        verify(digitsEventLogger1).confirmationCodeSuccess(details);
        verify(digitsEventLogger2).confirmationCodeSuccess(details);
    }

    @Test
    public void testSignupFailure() {
        digitsEventCollector.signupFailure();
        verify(digitsScribeClient).failure(Component.SIGNUP);
    }

    @Test
    public void testSignupException() {
        digitsEventCollector.signupException(exception);
        verify(digitsScribeClient).error(Component.SIGNUP, exception);
    }

    //Pin screen events
    @Test
    public void testPinScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.pinScreenImpression(details);
        verify(failFastEventDetailsChecker).twoFactorPinImpression(details);
        verify(digitsScribeClient).impression(Component.PIN);
        verify(digitsEventLogger1).twoFactorPinImpression(details);
        verify(digitsEventLogger2).twoFactorPinImpression(details);
    }

    @Test
    public void testSubmitClickOnPinScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitClickOnPinScreen(details);
        verify(failFastEventDetailsChecker).twoFactorPinSubmit(details);
        verify(digitsScribeClient).click(Component.PIN, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger1).twoFactorPinSubmit(details);
        verify(digitsEventLogger2).twoFactorPinSubmit(details);
    }

    @Test
    public void testTwoFactorPinVerificationSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.twoFactorPinVerificationSuccess(details);
        verify(failFastEventDetailsChecker).twoFactorPinSuccess(details);
        verify(digitsScribeClient).success(Component.PIN);
        verify(digitsEventLogger1).twoFactorPinSuccess(details);
        verify(digitsEventLogger2).twoFactorPinSuccess(details);
    }

    @Test
    public void testTwoFactorPinVerificationFailure() {
        digitsEventCollector.twoFactorPinVerificationFailure();
        verify(digitsScribeClient).failure(Component.PIN);
    }

    @Test
    public void testTwoFactorPinVerificationException() {
        digitsEventCollector.twoFactorPinVerificationException(exception);
        verify(digitsScribeClient).error(Component.PIN, exception);
    }

    //Email screen events
    @Test
    public void testEmailScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.emailScreenImpression(details);
        verify(failFastEventDetailsChecker).emailImpression(details);
        verify(digitsScribeClient).impression(Component.EMAIL);
        verify(digitsEventLogger1).emailImpression(details);
        verify(digitsEventLogger2).emailImpression(details);
    }

    @Test
    public void testSubmitClickOnEmailScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitClickOnEmailScreen(details);
        verify(failFastEventDetailsChecker).emailSubmit(details);
        verify(digitsScribeClient).click(Component.EMAIL, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger1).emailSubmit(details);
        verify(digitsEventLogger2).emailSubmit(details);
    }

    @Test
    public void testSubmitEmailSuccess() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.submitEmailSuccess(details);
        verify(failFastEventDetailsChecker).emailSuccess(details);
        verify(digitsScribeClient).success(Component.EMAIL);
        verify(digitsEventLogger1).emailSuccess(details);
        verify(digitsEventLogger2).emailSuccess(details);
    }

    @Test
    public void testSubmitEmailFailure() {
        digitsEventCollector.submitEmailFailure();
        verify(digitsScribeClient).failure(Component.EMAIL);
    }

    @Test
    public void testSubmitEmailException() {
        digitsEventCollector.submitEmailException(exception);
        verify(digitsScribeClient).error(Component.EMAIL, exception);
    }
    //Contacts upload screen events
    @Test
    public void testContactScreenImpression() {
        digitsEventCollector.contactScreenImpression();
        verify(digitsScribeClient).impression(Component.CONTACTS);
    }

    @Test
    public void testCancelClickOnContactScreen() {
        digitsEventCollector.cancelClickOnContactScreen();
        verify(digitsScribeClient).click(Component.CONTACTS, DigitsScribeConstants.Element.CANCEL);
    }

    @Test
    public void testBackClickOnContactScreen() {
        digitsEventCollector.backClickOnContactScreen();
        verify(digitsScribeClient).click(Component.CONTACTS, DigitsScribeConstants.Element.BACK);
    }

    @Test
    public void testSubmitClickOnContactScreen() {
        digitsEventCollector.submitClickOnContactScreen();
        verify(digitsScribeClient).click(Component.CONTACTS, DigitsScribeConstants.Element.SUBMIT);
    }

    //Failure screen events
    @Test
    public void testFailureScreenImpression() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.failureScreenImpression(details);
        verify(failFastEventDetailsChecker).failureImpression(details);
        verify(digitsScribeClient).impression(Component.FAILURE);
        verify(digitsEventLogger1).failureImpression(details);
        verify(digitsEventLogger2).failureImpression(details);
    }

    @Test
    public void testRetryClickOnFailureScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.retryClickOnFailureScreen(details);
        verify(failFastEventDetailsChecker).failureRetryClick(details);
        verify(digitsScribeClient).click(Component.FAILURE, DigitsScribeConstants.Element.RETRY);
        verify(digitsEventLogger1).failureRetryClick(details);
        verify(digitsEventLogger2).failureRetryClick(details);
    }

    @Test
    public void testDismissClickOnFailureScreen() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger2);
        digitsEventCollector.dismissClickOnFailureScreen(details);
        verify(failFastEventDetailsChecker).failureDismissClick(details);
        verify(digitsScribeClient).click(Component.FAILURE, DigitsScribeConstants.Element.DISMISS);
        verify(digitsEventLogger1).failureDismissClick(details);
        verify(digitsEventLogger2).failureDismissClick(details);
    }

    @Test
    public void testLoggerDeduping() {
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger1);
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger1);
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger1);
        digitsEventCollector.phoneScreenImpression(details);
        verify(digitsScribeClient).impression(Component.AUTH);
        verify(digitsEventLogger1, times(1)).phoneNumberImpression(details);
    }

    @Test
    public void testContactScreenImpression_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger);
        testContactScreenImpression();
        verify(digitsEventLogger).contactsPermissionImpression();
    }

    @Test
    public void testCancelClickOnContactScreen_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger);
        testCancelClickOnContactScreen();
        verify(digitsEventLogger).contactsPermissionCancel();
    }

    @Test
    public void testSubmitClickOnContactScreen_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.addDigitsEventLogger(digitsEventLogger);
        testSubmitClickOnContactScreen();
        verify(digitsEventLogger).contactsPermissionSubmit();
    }
}
