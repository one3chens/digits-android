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

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DigitsEventCollectorTest {
    @Captor
    private ArgumentCaptor<EventNamespace> eventNamespaceArgumentCaptor;
    private final DigitsScribeClient digitsScribeClient = mock(DigitsScribeClient.class);
    private DigitsEventCollector digitsEventCollector =
            new DigitsEventCollector(digitsScribeClient);
    private final DigitsException exception = new DigitsException("exception");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        digitsEventCollector = new DigitsEventCollector(digitsScribeClient);
    }

    @Test
    public void testAuthImpression() {
        digitsEventCollector.authImpression();
        verify(digitsScribeClient).impression(Component.EMPTY);
    }

    @Test
    public void testAuthSuccess() {
        digitsEventCollector.authSuccess();
        verify(digitsScribeClient).loginSuccess();
    }

    @Test
    public void testAuthFailure() {
        digitsEventCollector.authFailure();
        verify(digitsScribeClient).failure(Component.EMPTY);
    }

    //Phone screen events
    @Test
    public void testPhoneScreenImpression() {
        digitsEventCollector.phoneScreenImpression();
        verify(digitsScribeClient).impression(Component.AUTH);
    }

    @Test
    public void testCountryCodeClickOnPhoneScreen() {
        digitsEventCollector.countryCodeClickOnPhoneScreen();
        verify(digitsScribeClient).click(Component.AUTH,
                DigitsScribeConstants.Element.COUNTRY_CODE);
    }

    @Test
    public void testSubmitClickOnPhoneScreen() {
        digitsEventCollector.submitClickOnPhoneScreen();
        verify(digitsScribeClient).click(Component.AUTH, DigitsScribeConstants.Element.SUBMIT);
    }

    @Test
    public void testRetryClickOnPhoneScreen() {
        digitsEventCollector.retryClickOnPhoneScreen();
        verify(digitsScribeClient).click(Component.AUTH, DigitsScribeConstants.Element.RETRY);
    }

    @Test
    public void testSubmitPhoneSuccess() {
        digitsEventCollector.submitPhoneSuccess();
        verify(digitsScribeClient).success(Component.AUTH);
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
        digitsEventCollector.loginScreenImpression();
        verify(digitsScribeClient).impression(Component.LOGIN);
    }

    @Test
    public void testSubmitClickOnLoginScreen() {
        digitsEventCollector.submitClickOnLoginScreen();
        verify(digitsScribeClient).click(Component.LOGIN, DigitsScribeConstants.Element.SUBMIT);
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
        digitsEventCollector.loginCodeSuccess();
        verify(digitsScribeClient).success(Component.LOGIN);
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
        digitsEventCollector.signupScreenImpression();
        verify(digitsScribeClient).impression(Component.SIGNUP);
    }

    @Test
    public void testSubmitClickOnSignupScreen() {
        digitsEventCollector.submitClickOnSignupScreen();
        verify(digitsScribeClient).click(Component.SIGNUP, DigitsScribeConstants.Element.SUBMIT);
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
        digitsEventCollector.signupSuccess();
        verify(digitsScribeClient).success(Component.SIGNUP);
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
        digitsEventCollector.pinScreenImpression();
        verify(digitsScribeClient).impression(Component.PIN);
    }

    @Test
    public void testSubmitClickOnPinScreen() {
        digitsEventCollector.submitClickOnPinScreen();
        verify(digitsScribeClient).click(Component.PIN, DigitsScribeConstants.Element.SUBMIT);
    }

    @Test
    public void testTwoFactorPinVerificationSuccess() {
        digitsEventCollector.twoFactorPinVerificationSuccess();
        verify(digitsScribeClient).success(Component.PIN);
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
        digitsEventCollector.emailScreenImpression();
        verify(digitsScribeClient).impression(Component.EMAIL);
    }

    @Test
    public void testSubmitClickOnEmailScreen() {
        digitsEventCollector.submitClickOnEmailScreen();
        verify(digitsScribeClient).click(Component.EMAIL, DigitsScribeConstants.Element.SUBMIT);
    }

    @Test
    public void testSubmitEmailSuccess() {
        digitsEventCollector.submitEmailSuccess();
        verify(digitsScribeClient).success(Component.EMAIL);
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
    public void testSubmitClickOnContactScreen() {
        digitsEventCollector.submitClickOnContactScreen();
        verify(digitsScribeClient).click(Component.CONTACTS, DigitsScribeConstants.Element.SUBMIT);
    }

    //Failure screen events
    @Test
    public void testFailureScreenImpression() {
        digitsEventCollector.failureScreenImpression();
        verify(digitsScribeClient).impression(Component.FAILURE);
    }

    @Test
    public void testRetryClickOnFailureScreen() {
        digitsEventCollector.retryClickOnFailureScreen();
        verify(digitsScribeClient).click(Component.FAILURE, DigitsScribeConstants.Element.RETRY);
    }

    @Test
    public void testDismissClickOnFailureScreen() {
        digitsEventCollector.dismissClickOnFailureScreen();
        verify(digitsScribeClient).click(Component.FAILURE, DigitsScribeConstants.Element.DISMISS);
    }

    //External Logger event delegators
    //We test methods that delegate to the external event logger separately
    //The tests above implicitly test for the case where the external event logger is not set.
    @Test
    public void testAuthImpression_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.setLoggerResultReceiver(digitsEventLogger);
        digitsEventCollector.authImpression();
        verify(digitsScribeClient).impression(Component.EMPTY);
        verify(digitsEventLogger).phoneNumberImpression();
    }

    @Test
    public void testAuthSuccess_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.setLoggerResultReceiver(digitsEventLogger);
        digitsEventCollector.authSuccess();
        verify(digitsScribeClient).loginSuccess();
        verify(digitsEventLogger).loginSuccess();
    }

    @Test
    public void testSubmitClickOnPhoneScreen_withExternalLogger(){
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.setLoggerResultReceiver(digitsEventLogger);
        digitsEventCollector.submitClickOnPhoneScreen();
        verify(digitsScribeClient).click(Component.AUTH, DigitsScribeConstants.Element.SUBMIT);
        verify(digitsEventLogger).phoneNumberSubmit();
    }

    @Test
    public void testSubmitPhoneSuccess_withExternalLogger() {
        final DigitsEventLogger digitsEventLogger = mock(DigitsEventLogger.class);
        digitsEventCollector.setLoggerResultReceiver(digitsEventLogger);
        digitsEventCollector.submitPhoneSuccess();
        verify(digitsScribeClient).success(Component.AUTH);
        verify(digitsEventLogger).phoneNumberSuccess();
    }
}
