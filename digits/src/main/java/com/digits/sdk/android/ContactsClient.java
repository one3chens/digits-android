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

import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.core.Callback;
import retrofit.client.Response;

public class ContactsClient {
    private final ContactsPreferenceManager prefManager;
    private ActivityClassManagerFactory activityClassManagerFactory;
    private final DigitsApiClientManager apiClientManager;
    private final Digits digits;

    ContactsClient() {
        this(Digits.getInstance(), new DigitsApiClientManager(), new ContactsPreferenceManager(),
                new ActivityClassManagerFactory());
    }

    ContactsClient(Digits digits, DigitsApiClientManager apiManager,
                   ContactsPreferenceManager prefManager,
                   ActivityClassManagerFactory activityClassManagerFactory) {
        this.digits = digits;
        this.apiClientManager = apiManager;
        this.prefManager = prefManager;
        this.activityClassManagerFactory = activityClassManagerFactory;
    }

    /**
     * First checks if user previously gave permission to upload contacts. If not, shows
     * dialog requesting permission to upload users contacts. If permission granted start
     * background service to upload contacts. Otherwise, do nothing.
     */
    public void startContactsUpload() {
        startContactsUpload(R.style.Digits_default);
    }

    /**
     * First checks if user previously gave permission to upload contacts. If not, shows
     * dialog requesting permission to upload users contacts. If permission granted start
     * background service to upload contacts. Otherwise, do nothing.
     *
     * @param themeResId Resource id of theme
     */
    public void startContactsUpload(int themeResId) {
        startContactsUpload(digits.getContext(), themeResId);
    }


    /**
     * Returns true if user has previously granted contacts upload permission. Otherwise, returns
     * false.
     */
    public boolean hasUserGrantedPermission() {
        return prefManager.hasContactImportPermissionGranted();
    }

    protected void startContactsUpload(Context context, int themeResId) {
        if (!hasUserGrantedPermission()) {
            startContactsActivity(context, themeResId);
        } else {
            startContactsService(context);
        }
    }

    private void startContactsActivity(Context context, int themeResId) {
        final ActivityClassManager activityClassManager =
                activityClassManagerFactory.createActivityClassManager(context, themeResId);
        final Intent intent = new Intent(context, activityClassManager.getContactsActivity());
        intent.putExtra(ThemeUtils.THEME_RESOURCE_ID, themeResId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void startContactsService(Context context) {
        context.startService(new Intent(context, ContactsUploadService.class));
    }

    protected ApiInterface getDigitsApiService() {
        return apiClientManager.getService();
    }

    /**
     * Deletes all uploaded contacts.
     *
     * @param callback to be executed on UI thread with HTTP response.
     */
    public void deleteAllUploadedContacts(final ContactsCallback<Response> callback) {
        getDigitsApiService().deleteAll(callback);
    }

    /**
     * Retrieve all matched contacts. Handles paging, and makes callback
     * when all matches are retrieved
     *
     * @param callback   to be executed on UI thread with matched users.
     */
    public void lookupContactMatchesStart(final Callback<Contacts> callback) {
        lookupContactMatches(null, 100, callback);
    }

    /**
     * Lookup matched contacts.
     *
     * @param nextCursor reference to next set of results. If null returns the first 100 users.
     * @param count      number of results to return. Min value is 1. Max value is 100. Default
     *                   value is 50. Values out of range will return default.
     * @param callback   to be executed on UI thread with matched users.
     */
    public void lookupContactMatches(final String nextCursor, final Integer count,
                                        final Callback<Contacts> callback) {
        if (count == null || count < 1 || count > 100) {
            getDigitsApiService().usersAndUploadedBy(nextCursor, null, callback);
        } else {
            getDigitsApiService().usersAndUploadedBy(nextCursor, count, callback);
        }
    }

    UploadResponse uploadContacts(Vcards vcards) {
        return getDigitsApiService().upload(vcards);
    }

}
