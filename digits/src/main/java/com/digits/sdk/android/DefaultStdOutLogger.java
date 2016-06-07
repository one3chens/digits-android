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

import io.fabric.sdk.android.Fabric;

class DefaultStdOutLogger extends DigitsEventLogger {
    final static String TAG = "DefaultStdOutLogger";
    final static DefaultStdOutLogger instance;

    static{
        instance = new DefaultStdOutLogger();
    }

    private DefaultStdOutLogger() {
    }

    @Override
    public void loginBegin(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void phoneNumberImpression(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void phoneNumberSubmit(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void phoneNumberSuccess(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void confirmationCodeImpression(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void confirmationCodeSubmit(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void confirmationCodeSuccess(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void twoFactorPinImpression(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void twoFactorPinSubmit(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void twoFactorPinSuccess(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void emailImpression(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void emailSubmit(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void emailSuccess(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void failureImpression(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void failureRetryClick(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void failureDismissClick(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void loginSuccess(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void loginFailure(DigitsEventDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsPermissionForDigitsImpression(
            ContactsPermissionForDigitsImpressionDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsPermissionForDigitsApproved(
            ContactsPermissionForDigitsApprovedDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsPermissionForDigitsDeferred(
            ContactsPermissionForDigitsDeferredDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsUploadStart(ContactsUploadStartDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsUploadSuccess(ContactsUploadSuccessDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsUploadFailure(ContactsUploadFailureDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsLookupStart(ContactsLookupStartDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsLookupSuccess(ContactsLookupSuccessDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsLookupFailure(ContactsLookupFailureDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsDeletionStart(ContactsDeletionStartDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsDeletionSuccess(ContactsDeletionSuccessDetails details) {
        logEvent(details);
    }

    @Override
    public void contactsDeletionFailure(ContactsDeletionFailureDetails details) {
        logEvent(details);
    }

    private <T> void logEvent(T details){
        Fabric.getLogger().d(TAG, details.toString());
    }
}

