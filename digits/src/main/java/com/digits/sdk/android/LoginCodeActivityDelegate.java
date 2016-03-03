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
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;

import io.fabric.sdk.android.services.common.CommonUtils;

class LoginCodeActivityDelegate extends DigitsActivityDelegateImpl {
    private final DigitsScribeService scribeService;
    EditText editText;
    LinkTextView editPhoneNumberLinkTextView;
    StateButton stateButton;
    InvertedStateButton resendButton, callMeButton;
    TextView termsText;
    TextView timerText;
    DigitsController controller;
    SmsBroadcastReceiver receiver;
    Activity activity;
    AuthConfig config;
    TosFormatHelper tosFormatHelper;

    LoginCodeActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override
    public void init(final Activity activity, Bundle bundle) {
        this.activity = activity;
        editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        resendButton =  (InvertedStateButton) activity
                .findViewById(R.id.dgts__resendConfirmationButton);
        callMeButton =  (InvertedStateButton) activity.findViewById(R.id.dgts__callMeButton);
        editPhoneNumberLinkTextView = (LinkTextView) activity
                .findViewById(R.id.dgts__editPhoneNumber);
        termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        timerText = (TextView) activity.findViewById(R.id.dgts__countdownTimer);
        config = bundle.getParcelable(DigitsClient.EXTRA_AUTH_CONFIG);

        controller = initController(bundle);
        tosFormatHelper = new TosFormatHelper(activity);

        setUpEditText(activity, controller, editText);
        setUpSendButton(activity, controller, stateButton);
        setupResendButton(activity, controller, scribeService, resendButton);
        setupCallMeButton(activity, controller, scribeService, callMeButton, config);
        setupCountDownTimer(controller, timerText, config);
        setUpEditPhoneNumberLink(activity, editPhoneNumberLinkTextView,
                bundle.getString(DigitsClient.EXTRA_PHONE));
        setUpTermsText(activity, controller, termsText);
        setUpSmsIntercept(activity, editText);

        CommonUtils.openKeyboard(activity, editText);
    }

    DigitsController initController(Bundle bundle) {
        return new LoginCodeController(bundle
                .<ResultReceiver>getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER),
                stateButton, resendButton, callMeButton, editText,
                bundle.getString(DigitsClient.EXTRA_REQUEST_ID),
                bundle.getLong(DigitsClient.EXTRA_USER_ID), bundle.getString(DigitsClient
                .EXTRA_PHONE), scribeService, bundle.getBoolean(DigitsClient.EXTRA_EMAIL),
                timerText);
    }

    @Override
    public void setUpSendButton(Activity activity, DigitsController controller,
                                StateButton stateButton) {
        stateButton.setStatesText(R.string.dgts__continue, R.string.dgts__sending,
                R.string.dgts__done);
        stateButton.showStart();
        super.setUpSendButton(activity, controller, stateButton);
    }

    @Override
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        if (config != null && config.tosUpdate) {
            termsText.setText(tosFormatHelper.getFormattedTerms(R.string.dgts__terms_text_updated));
        } else {
            termsText.setText(tosFormatHelper.getFormattedTerms(R.string.dgts__terms_text_sign_in));
        }
        super.setUpTermsText(activity, controller, termsText);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dgts__activity_confirmation;
    }

    @Override
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, DigitsClient.EXTRA_RESULT_RECEIVER,
                DigitsClient.EXTRA_PHONE, DigitsClient.EXTRA_REQUEST_ID,
                DigitsClient.EXTRA_USER_ID);
    }

    @Override
    public void onResume() {
        scribeService.impression();
        controller.onResume();
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
        }
        controller.cancelTimer();
    }

    protected void setUpSmsIntercept(Activity activity, EditText editText) {
        if (CommonUtils.checkPermission(activity, "android.permission.RECEIVE_SMS")) {
            final IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            receiver = new SmsBroadcastReceiver(editText);
            activity.registerReceiver(receiver, filter);
        }
    }
}
