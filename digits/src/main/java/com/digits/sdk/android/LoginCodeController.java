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
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;

import io.fabric.sdk.android.services.common.CommonUtils;


class LoginCodeController extends DigitsControllerImpl {
    private final long userId;
    private final String phoneNumber;
    private final Boolean emailCollection;
    private final InvertedStateButton resendButton, callMeButton;
    private String requestId;
    private final TextView timerText;

    LoginCodeController(ResultReceiver resultReceiver, StateButton stateButton,
                        InvertedStateButton resendButton, InvertedStateButton callMeButton,
                        EditText phoneEditText, String requestId, long userId, String phoneNumber,
                        DigitsEventCollector digitsEventCollector, Boolean emailCollection,
                        TextView timerText, DigitsEventDetailsBuilder details) {
        this(resultReceiver, stateButton, resendButton, callMeButton, phoneEditText,
                Digits.getSessionManager(), Digits.getInstance().getAuthClient(), requestId,
                userId, phoneNumber,
                new ConfirmationErrorCodes(stateButton.getContext().getResources()),
                Digits.getInstance().getActivityClassManager(), digitsEventCollector,
                emailCollection, timerText, details);
    }

    LoginCodeController(ResultReceiver resultReceiver,
                        StateButton stateButton, InvertedStateButton resendButton,
                        InvertedStateButton callMeButton, EditText loginEditText,
                        SessionManager<DigitsSession> sessionManager, AuthClient client,
                        String requestId, long userId, String phoneNumber, ErrorCodes errors,
                        ActivityClassManager activityClassManager,
                        DigitsEventCollector digitsEventCollector, Boolean emailCollection,
                        TextView timerText, DigitsEventDetailsBuilder details) {
        super(resultReceiver, stateButton, loginEditText, client, errors, activityClassManager,
                sessionManager, digitsEventCollector, details);
        this.requestId = requestId;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.emailCollection = emailCollection;
        this.resendButton = resendButton;
        this.callMeButton = callMeButton;
        this.countDownTimer = createCountDownTimer(
                DigitsConstants.RESEND_TIMER_DURATION_MILLIS, timerText, resendButton,
                callMeButton);
        this.timerText = timerText;
    }

    @Override
    public void executeRequest(final Context context) {
        digitsEventCollector.submitClickOnLoginScreen(
                eventDetailsBuilder.withCurrentTime(System.currentTimeMillis()).build());
        if (validateInput(editText.getText())) {
            sendButton.showProgress();
            CommonUtils.hideKeyboard(context, editText);
            final String code = editText.getText().toString();
            authClient.loginDevice(requestId, userId, code,
                    new DigitsCallback<DigitsSessionResponse>(context, this) {
                        public void success(Result<DigitsSessionResponse> result) {
                            digitsEventCollector.loginCodeSuccess(
                                    eventDetailsBuilder
                                            .withCurrentTime(System.currentTimeMillis())
                                            .build());
                            if (result.data.isEmpty()) {
                                startPinCodeActivity(context);
                            } else {
                                final DigitsSession session =
                                        DigitsSession.create(result.data, phoneNumber);
                                sessionManager.setActiveSession(session);
                                if (emailCollection) {
                                    emailRequest(context, session);
                                } else {
                                    loginSuccess(context, session, phoneNumber,
                                            eventDetailsBuilder);
                                }
                            }
                        }
                    });
        } else {
            handleError(context,
              new DigitsException(
                errors.getMessage(DigitsApiErrorConstants.CLIENT_SIDE_VALIDATION_FAILED)));
        }
    }

    //Config responses during resends are currently ignored
    public void resendCode(final Context context, final InvertedStateButton activeButton,
                           final Verification verificationType) {
        activeButton.showProgress();
        authClient.authDevice(phoneNumber, verificationType,
                new DigitsCallback<AuthResponse>(context, this) {
                    @Override
                    public void success(final Result<AuthResponse> result) {
                        activeButton.showFinish();
                        requestId = result.data.requestId;
                        activeButton.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activeButton.showStart();
                                timerText.setText(
                                  String.valueOf(
                                    DigitsConstants.RESEND_TIMER_DURATION_MILLIS / 1000),
                                  TextView.BufferType.NORMAL);
                                resendButton.setEnabled(false);
                                callMeButton.setEnabled(false);
                                startTimer();
                            }
                        }, POST_DELAY_MS);

                    }
                }
        );
    }

    @Override
    public void scribeControllerFailure() {
        digitsEventCollector.loginFailure();
    }

    @Override
    void scribeControllerException(DigitsException exception) {
        digitsEventCollector.loginException(exception);
    }

    @Override
    public void handleError(final Context context, DigitsException digitsException) {
        callMeButton.showError();
        resendButton.showError();
        super.handleError(context, digitsException);
    }

    private void emailRequest(final Context context, final DigitsSession session) {
        getAccountService().verifyAccount
                (new DigitsCallback<VerifyAccountResponse>(context, this) {
                    @Override
                    public void success(Result<VerifyAccountResponse> result) {
                        final DigitsSession newSession =
                                DigitsSession.create(result.data);
                        if (canRequestEmail(newSession, session)) {
                            startEmailRequest(context, phoneNumber,
                                    LoginCodeController.this.eventDetailsBuilder);
                        } else {
                            loginSuccess(context, newSession, phoneNumber,
                                    LoginCodeController.this.eventDetailsBuilder);
                        }
                    }
                });
    }

    private void startPinCodeActivity(Context context) {
        final Intent intent = new Intent(context, activityClassManager.getPinCodeActivity());
        final Bundle bundle = getBundle(phoneNumber, eventDetailsBuilder);
        bundle.putParcelable(AuthClient.EXTRA_RESULT_RECEIVER, resultReceiver);
        bundle.putString(AuthClient.EXTRA_REQUEST_ID, requestId);
        bundle.putLong(AuthClient.EXTRA_USER_ID, userId);
        bundle.putBoolean(AuthClient.EXTRA_EMAIL, emailCollection);
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
        finishActivity(context);
    }

    @Override
    public boolean validateInput(CharSequence text) {
        return super.validateInput(text) &&
                text.length() >= DigitsConstants.MIN_CONFIRMATION_CODE_LENGTH;
    }

    @Override
    Uri getTosUri() {
        return DigitsConstants.TWITTER_TOS;
    }

    private boolean canRequestEmail(DigitsSession newSession, DigitsSession session) {
        return emailCollection && newSession.getEmail().equals(DigitsSession.DEFAULT_EMAIL)
                && newSession.getId() == session.getId();
    }

    ApiInterface getAccountService() {
        return Digits.getInstance().getApiClientManager().getApiClient().getService();
    }

}
