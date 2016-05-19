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

import android.util.Log;

import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLSocketFactory;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;

class DigitsApiClient {
    private final ApiInterface service;
    private final Session session;
    private static final String NULL_SESSION_ERROR_LOG =
            "Attempting to connect to Digits API with null session. " +
                    "Please re-authenticate and try again";

    DigitsApiClient(Session session) {
        this(session, TwitterCore.getInstance(), TwitterCore.getInstance().getSSLSocketFactory(),
                Digits.getInstance()
                        .getExecutorService());
    }

    DigitsApiClient(Session session, TwitterCore twitterCore, SSLSocketFactory sslFactory,
                    ExecutorService executorService) {
        this(session, twitterCore, sslFactory, executorService, new DigitsUserAgent());
    }

    DigitsApiClient(Session session, TwitterCore twitterCore,
                    SSLSocketFactory sslFactory, ExecutorService executorService,
                    DigitsUserAgent userAgent) {
        if (session == null) {
            Log.e(Digits.TAG, NULL_SESSION_ERROR_LOG);
        }
        this.session = session;

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(new DigitsApi().getBaseHostUrl())
                .setRequestInterceptor(new DigitsRequestInterceptor(userAgent))
                .setExecutors(executorService, new MainThreadExecutor())
                .setClient(
                        new AuthenticatedClient(twitterCore.getAuthConfig(), session, sslFactory))
                .build();
        this.service = restAdapter.create(ApiInterface.class);

    }

    public Session getSession() {
        return session;
    }

    public ApiInterface getService() {
        return service;
    }

}
