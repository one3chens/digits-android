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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class DigitsActivityDelegateImplTests extends DigitsActivityDelegateTests
        <DigitsActivityDelegateImplTests.MockDigitsActivityDelegateImpl> {
    static final Integer COUNTRY_CODE = 123;
    static final String PHONE_WITH_COUNTRY_CODE = "+" + COUNTRY_CODE + "123456789";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        doReturn(layoutParams).when(timerText).getLayoutParams();
        doReturn(timer).when(controller).getCountDownTimer(
                DigitsConstants.RESEND_TIMER_DURATION_MILLIS, timerText, resendButton,
                callMeButton);
    }

    @Override
    public MockDigitsActivityDelegateImpl getDelegate() {
        return new MockDigitsActivityDelegateImpl();
    }

    public void testSetupResendButton() throws Exception {
        delegate.setupResendButton(activity, controller, scribeService, resendButton);
        verify(resendButton).setEnabled(false);
    }

    public void testSetupCallMeButton_voiceEnabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.TRUE;

        delegate.setupCallMeButton(activity, controller, scribeService, callMeButton, config);
        verify(callMeButton).setEnabled(false);
        verify(callMeButton).setVisibility(View.VISIBLE);
    }

    public void testSetupCallMeButton_voiceDisabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.FALSE;

        delegate.setupCallMeButton(activity, controller, scribeService, callMeButton, config);
        verify(callMeButton).setEnabled(false);
        verify(callMeButton).setVisibility(View.GONE);
    }

    public void testSetupTimerText_voiceEnabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.TRUE;

        delegate.setupCountDownTimer(timerText, timer, config);

        verify(layoutParams).addRule(RelativeLayout.ALIGN_RIGHT,
                R.id.dgts__callMeButton);
        verify(layoutParams).addRule(RelativeLayout.ALIGN_BOTTOM,
                R.id.dgts__callMeButton);
        verify(timer).start();

    }

    public void testSetupTimerText_voiceDisabled() throws Exception {
        final AuthConfig config = new AuthConfig();
        config.isVoiceEnabled = Boolean.FALSE;

        delegate.setupCountDownTimer(timerText, timer, config);

        verify(layoutParams).addRule(RelativeLayout.ALIGN_RIGHT,
                R.id.dgts__resendConfirmationButton);
        verify(layoutParams).addRule(RelativeLayout.ALIGN_BOTTOM,
                R.id.dgts__resendConfirmationButton);
        verify(timer).start();

    }

    public void testSetUpEditPhoneLink() throws Exception {
        delegate.setUpEditPhoneNumberLink(activity, editPhoneNumberLink,
                PHONE_WITH_COUNTRY_CODE);
        verify(editPhoneNumberLink).setText(PHONE_WITH_COUNTRY_CODE);
        verify(editPhoneNumberLink).setOnClickListener(captorClick.capture());

        final View.OnClickListener listener = captorClick.getValue();
        listener.onClick(null);

        verify(activity).finish();
        verifyResultCode(activity, DigitsActivity.RESULT_CHANGE_PHONE_NUMBER);
    }

    public void testInitTimerTest() throws Exception{
        delegate.initCountDownTimer(controller, timerText, resendButton, callMeButton);
        verify(controller).getCountDownTimer(DigitsConstants.RESEND_TIMER_DURATION_MILLIS,
                timerText, resendButton, callMeButton);
    }

    class MockDigitsActivityDelegateImpl extends DigitsActivityDelegateImpl {
        @Override
        public int getLayoutId() {
            return 0;
        }

        @Override
        public boolean isValid(Bundle bundle) {
            return false;
        }

        @Override
        public void init(Activity activity, Bundle bundle) {

        }

        @Override
        public void onResume() {

        }
    }
}
