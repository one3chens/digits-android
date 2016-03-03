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
import android.os.ResultReceiver;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.fabric.sdk.android.services.common.CommonUtils;

public class EmailRequestActivityDelegate extends DigitsActivityDelegateImpl {
    EditText editText;
    StateButton stateButton;
    InvertedStateButton resendButton, callMeButton;
    LinkTextView editPhoneNumberLink;
    TextView termsText;
    TextView timerText;
    DigitsController controller;
    Activity activity;
    DigitsScribeService scribeService;
    TextView titleText;
    TosFormatHelper tosFormatHelper;

    EmailRequestActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dgts__activity_confirmation;
    }

    @Override
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, DigitsClient.EXTRA_RESULT_RECEIVER,
                DigitsClient.EXTRA_PHONE);
    }

    @Override
    public void init(Activity activity, Bundle bundle) {
        this.activity = activity;
        titleText = (TextView) activity.findViewById(R.id.dgts__titleText);
        editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        resendButton =  (InvertedStateButton) activity
                .findViewById(R.id.dgts__resendConfirmationButton);
        callMeButton =  (InvertedStateButton) activity.findViewById(R.id.dgts__callMeButton);
        editPhoneNumberLink = (LinkTextView) activity.findViewById(R.id.dgts__editPhoneNumber);
        termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        timerText = (TextView) activity.findViewById(R.id.dgts__countdownTimer);
        final AuthConfig config = bundle.getParcelable(DigitsClient.EXTRA_AUTH_CONFIG);

        controller = initController(bundle);
        tosFormatHelper = new TosFormatHelper(activity);

        editText.setHint(R.string.dgts__email_request_edit_hint);
        titleText.setText(R.string.dgts__email_request_title);

        setUpEditText(activity, controller, editText);
        setUpSendButton(activity, controller, stateButton);
        setupResendButton(activity, controller, scribeService, resendButton);
        setupCallMeButton(activity, controller, scribeService, callMeButton, config);
        setupCountDownTimer(controller, timerText, config);
        setUpEditPhoneNumberLink(activity, editPhoneNumberLink,
                bundle.getString(DigitsClient.EXTRA_PHONE));
        setUpTermsText(activity, controller, termsText);

        CommonUtils.openKeyboard(activity, editText);
    }

    @Override
    protected void setUpEditPhoneNumberLink(final Activity activity,
                                            final LinkTextView editPhoneLink,
                                            String phoneNumber) {
        editPhoneLink.setVisibility(View.GONE);
    }

    @Override
    void setupResendButton(final Activity activity, final DigitsController controller,
                           final DigitsScribeService scribeService,
                           final InvertedStateButton resendButton){
        resendButton.setVisibility(View.GONE);
    }

    @Override
    void setupCallMeButton(final Activity activity, final DigitsController controller,
                           final DigitsScribeService scribeService,
                           final InvertedStateButton callMeButton,
                           final AuthConfig config){
        callMeButton.setVisibility(View.GONE);
    }

    @Override
    void setupCountDownTimer(final DigitsController controller,
                             final TextView timerText,
                             final AuthConfig config){
        timerText.setVisibility(View.GONE);
    }

    @Override
    public void setUpEditText(final Activity activity, final DigitsController controller,
                              EditText editText) {
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        super.setUpEditText(activity, controller, editText);
    }

    @Override
    public void setUpSendButton(final Activity activity, final DigitsController controller,
                                StateButton stateButton) {
        stateButton.setStatesText(R.string.dgts__continue, R.string.dgts__sending,
                R.string.dgts__done);
        stateButton.showStart();
        super.setUpSendButton(activity, controller, stateButton);
    }

    @Override
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        termsText.setText(tosFormatHelper.getFormattedTerms(R.string.dgts__terms_email_request));
        super.setUpTermsText(activity, controller, termsText);
    }

    private DigitsController initController(Bundle bundle) {
        return new EmailRequestController(stateButton, editText,
                bundle.<ResultReceiver>getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER),
                bundle.getString(DigitsClient.EXTRA_PHONE), scribeService);
    }

    @Override
    public void onResume() {
        scribeService.impression();
        controller.onResume();
    }
}
