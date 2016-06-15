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
package com.example.app.digits;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import com.digits.sdk.android.ContactsDeletionFailureDetails;
import com.digits.sdk.android.ContactsDeletionStartDetails;
import com.digits.sdk.android.ContactsDeletionSuccessDetails;
import com.digits.sdk.android.ContactsLookupFailureDetails;
import com.digits.sdk.android.ContactsLookupStartDetails;
import com.digits.sdk.android.ContactsLookupSuccessDetails;
import com.digits.sdk.android.ContactsPermissionForDigitsApprovedDetails;
import com.digits.sdk.android.ContactsPermissionForDigitsDeferredDetails;
import com.digits.sdk.android.ContactsPermissionForDigitsImpressionDetails;
import com.digits.sdk.android.ContactsUploadFailureDetails;
import com.digits.sdk.android.ContactsUploadStartDetails;
import com.digits.sdk.android.ContactsUploadSuccessDetails;
import com.digits.sdk.android.DigitsEventLogger;
import com.digits.sdk.android.DigitsEventDetails;
import com.digits.sdk.android.LogoutEventDetails;

public class AnswersLogger extends DigitsEventLogger {
    @Override
    public void loginBegin(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "loginBegin")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void phoneNumberSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "confirmationCodeImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "confirmationCodeSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void confirmationCodeSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "confirmationCodeSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "twoFactorPinImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "twoFactorPinSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void twoFactorPinSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "twoFactorPinSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "emailImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailSubmit(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "emailSubmit")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void emailSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "emailSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureImpression(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "failureImpression")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureRetryClick(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "failureRetryClick")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void failureDismissClick(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "failureDismissClick")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void loginSuccess(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "loginSuccess")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void loginFailure(DigitsEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "loginFailure")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("ElapsedTime", details.elapsedTimeInMillis / 1000));
    }

    @Override
    public void logout(LogoutEventDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "logout")
                .putCustomAttribute("Language", details.language)
                .putCustomAttribute("Country", details.country));
    }

    @Override
    public void contactsPermissionForDigitsImpression(
            ContactsPermissionForDigitsImpressionDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Permissions-Digits")
                .putCustomAttribute("Action", "contactsPermissionForDigitsImpression"));
    }

    @Override
    public void contactsPermissionForDigitsApproved(
            ContactsPermissionForDigitsApprovedDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Permissions-Digits")
                .putCustomAttribute("Action", "contactsPermissionForDigitsApproved"));
    }

    @Override
    public void contactsPermissionForDigitsDeferred(
            ContactsPermissionForDigitsDeferredDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Permissions-Digits")
                .putCustomAttribute("Action", "contactsPermissionForDigitsDeferred"));
    }

    @Override
    public void contactsUploadStart(ContactsUploadStartDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Upload-Digits")
                .putCustomAttribute("Action", "contactsUploadStart"));
    }

    @Override
    public void contactsUploadSuccess(ContactsUploadSuccessDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Upload-Digits")
                .putCustomAttribute("Action", "contactsUploadSuccess")
                .putCustomAttribute("Uploaded", details.successContacts)
                .putCustomAttribute("Total", details.totalContacts));
    }

    @Override
    public void contactsUploadFailure(ContactsUploadFailureDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Upload-Digits")
                .putCustomAttribute("Action", "contactsUploadFailure")
                .putCustomAttribute("Failed", details.failedContacts)
                .putCustomAttribute("Total", details.totalContacts));
    }

    @Override
    public void contactsLookupStart(ContactsLookupStartDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Lookup-Digits")
                .putCustomAttribute("Action", "contactsLookupStart"));
    }

    @Override
    public void contactsLookupSuccess(ContactsLookupSuccessDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Lookup-Digits")
                .putCustomAttribute("Action", "contactsLookupSuccess")
                .putCustomAttribute("Matches", details.matchCount));
    }

    @Override
    public void contactsLookupFailure(ContactsLookupFailureDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Lookup-Digits")
                .putCustomAttribute("Action", "contactsLookupFailure"));
    }

    @Override
    public void contactsDeletionStart(ContactsDeletionStartDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Deletion-Digits")
                .putCustomAttribute("Action", "contactsDeletionStart"));
    }

    @Override
    public void contactsDeletionSuccess(ContactsDeletionSuccessDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Deletion-Digits")
                .putCustomAttribute("Action", "contactsDeletionSuccess"));
    }

    @Override
    public void contactsDeletionFailure(ContactsDeletionFailureDetails details) {
        Answers.getInstance().logCustom(new CustomEvent("Contacts-Deletion-Digits")
                .putCustomAttribute("Action", "contactsDeletionFailure"));
    }
}