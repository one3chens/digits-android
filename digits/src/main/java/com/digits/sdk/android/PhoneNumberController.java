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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.twitter.sdk.android.core.SessionManager;

import java.util.Locale;

import io.fabric.sdk.android.services.common.CommonUtils;

class PhoneNumberController extends DigitsControllerImpl implements
        PhoneNumberTask.Listener {
    private final TosView tosView;
    final CountryListSpinner countryCodeSpinner;
    boolean voiceEnabled;
    boolean resendState;
    boolean emailCollection;

    PhoneNumberController(ResultReceiver resultReceiver,
                          StateButton stateButton, EditText phoneEditText,
                          CountryListSpinner countryCodeSpinner, TosView tosView,
                          DigitsEventCollector digitsEventCollector, boolean emailCollection) {
        this(resultReceiver, stateButton, phoneEditText, countryCodeSpinner,
                Digits.getInstance().getDigitsClient(), new PhoneNumberErrorCodes(stateButton
                        .getContext().getResources()),
                Digits.getInstance().getActivityClassManager(), Digits.getSessionManager(),
                tosView, digitsEventCollector, emailCollection);
    }

    /**
     * Only for test
     */
    PhoneNumberController(ResultReceiver resultReceiver, StateButton stateButton,
                          EditText phoneEditText, CountryListSpinner countryCodeSpinner,
                          DigitsClient client, ErrorCodes errors,
                          ActivityClassManager activityClassManager,
                          SessionManager<DigitsSession> sessionManager, TosView tosView,
                          DigitsEventCollector digitsEventCollector, boolean emailCollection) {
        super(resultReceiver, stateButton, phoneEditText, client, errors, activityClassManager,
                sessionManager, digitsEventCollector);
        this.countryCodeSpinner = countryCodeSpinner;
        this.tosView = tosView;
        voiceEnabled = false;
        resendState = false;
        this.emailCollection = emailCollection;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        if (PhoneNumber.isValid(phoneNumber)) {
            editText.setText(phoneNumber.getPhoneNumber());
            editText.setSelection(phoneNumber.getPhoneNumber().length());
        }
    }

    public void setCountryCode(PhoneNumber phoneNumber) {
        if (PhoneNumber.isCountryValid(phoneNumber)) {
            countryCodeSpinner.setSelectedForCountry(new Locale("",
                    phoneNumber.getCountryIso()).getDisplayName(), phoneNumber.getCountryCode());
        }
    }

    LoginOrSignupComposer createCompositeCallback(final Context context, final String phoneNumber) {
        return new LoginOrSignupComposer(context, digitsClient, phoneNumber,
                getVerificationType(), this.emailCollection, resultReceiver,
                activityClassManager) {

            @Override
            public void success(final Intent intent) {
                sendButton.showFinish();

                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        digitsEventCollector.submitPhoneSuccess();
                        startActivityForResult((Activity) context, intent);
                    }
                }, POST_DELAY_MS);
            }

            @Override
            public void failure(DigitsException digitsException) {
                if (digitsException instanceof OperatorUnsupportedException) {
                    voiceEnabled = digitsException.getConfig().isVoiceEnabled;
                    resend();
                    PhoneNumberController.this.handleError(context, digitsException);
                } else {
                    PhoneNumberController.this.handleError(context, digitsException);
                }
            }
        };
    }

    @Override
    public void executeRequest(final Context context) {
        scribeRequest();
        if (validateInput(editText.getText())) {
            sendButton.showProgress();
            CommonUtils.hideKeyboard(context, editText);
            final int code = (Integer) countryCodeSpinner.getTag();
            final String number = editText.getText().toString();
            final String phoneNumber = getNumber(code, number);
            createCompositeCallback(context, phoneNumber).start();
        }
    }

    private void scribeRequest() {
        if (isRetry()) {
            digitsEventCollector.retryClickOnPhoneScreen();
        } else {
            digitsEventCollector.submitClickOnPhoneScreen();
        }
    }

    private boolean isRetry() {
        return errorCount > 0;
    }

    @NonNull
    private Verification getVerificationType() {
        return resendState && voiceEnabled ? Verification.voicecall : Verification.sms;
    }

    @Override
    Uri getTosUri() {
        return DigitsConstants.DIGITS_TOS;
    }

    private String getNumber(long countryCode, String numberTextView) {
        return "+" + String.valueOf(countryCode) + numberTextView;
    }

    public void onLoadComplete(PhoneNumber phoneNumber) {
        setPhoneNumber(phoneNumber);
        setCountryCode(phoneNumber);
    }


    public void resend() {
        resendState = true;
        if (voiceEnabled) {
            sendButton.setStatesText(R.string.dgts__call_me, R.string.dgts__calling,
                    R.string.dgts__calling);
            tosView.setText(R.string.dgts__terms_text_call_me);
        }
    }

    @Override
    public void scribeControllerFailure() {
        digitsEventCollector.submitPhoneFailure();
    }

    @Override
    void scribeControllerException(DigitsException exception) {
        digitsEventCollector.submitPhoneException(exception);
    }

    @Override
    public void startFallback(Context context, ResultReceiver receiver, DigitsException reason) {
        final Intent intent = new Intent(context, activityClassManager.getFailureActivity());
        intent.putExtra(DigitsClient.EXTRA_RESULT_RECEIVER, receiver);
        intent.putExtra(DigitsClient.EXTRA_FALLBACK_REASON, reason);
        context.startActivity(intent);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (Verification.voicecall.equals(getVerificationType())) {
            resendState = false;
            sendButton.setStatesText(R.string.dgts__continue,
                    R.string.dgts__sending,
                    R.string.dgts__done);
            sendButton.showStart();
            tosView.setText(R.string.dgts__terms_text);
        }
    }
}
