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

import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.concurrent.ExecutorService;

public class DigitsApiClientManager {
    private final SessionManager<DigitsSession> sessionManager;
    private final TwitterCore twitterCore;
    private final ExecutorService executorService;
    private final DigitsRequestInterceptor interceptor;
    private boolean sandboxEnabled;
    private ApiInterface mockInterface;

    private DigitsApiClient digitsApiClient;

    DigitsApiClientManager(SessionManager<DigitsSession> sessionManager) {
        this(new DigitsUserAgent(), TwitterCore.getInstance(),
                Digits.getInstance().getExecutorService(), sessionManager, null,
                new DigitsRequestInterceptor(new DigitsUserAgent()));
    }

    DigitsApiClientManager(DigitsUserAgent userAgent, TwitterCore twitterCore,
                           ExecutorService executorService,
                           SessionManager<DigitsSession> sessionManager,
                           DigitsApiClient apiClient,
                           DigitsRequestInterceptor interceptor) {
        if (twitterCore == null) {
            throw new IllegalArgumentException("twitter must not be null");
        }
        if (userAgent == null) {
            throw new IllegalArgumentException("userAgent must not be null");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager must not be null");
        }

        this.twitterCore = twitterCore;
        this.executorService = executorService;
        this.sessionManager = sessionManager;
        this.interceptor = interceptor;
        this.sandboxEnabled = false;
        this.mockInterface = new MockApiInterface();

        if (apiClient != null) {
            this.digitsApiClient = apiClient;
        } else {
            this.digitsApiClient = createNewClient();
        }
    }

    DigitsApiClientManager enableSandbox(){
        sandboxEnabled = true;
        this.digitsApiClient = createNewClient();
        return this;
    }

    DigitsApiClientManager disableSandbox(){
        sandboxEnabled = false;
        this.digitsApiClient = createNewClient();
        return this;
    }

    DigitsApiClientManager setSandboxClient(ApiInterface mockInterface){
        this.mockInterface = mockInterface;
        return this;
    }

    DigitsApiClient getApiClient(){
        if (sessionManager.getActiveSession() == null ||
                !sessionManager.getActiveSession().equals(digitsApiClient.getSession())) {
            this.digitsApiClient = createNewClient();
        }
        return digitsApiClient;
    }

    ApiInterface getService(){
          return getApiClient().getService();
    }

    protected DigitsApiClient createNewClient(){
        if (sandboxEnabled) {
            return new DigitsApiClient(sessionManager.getActiveSession(), twitterCore,
                    twitterCore.getSSLSocketFactory(),
                    executorService, interceptor, mockInterface);
        } else {
            return new DigitsApiClient(sessionManager.getActiveSession(), twitterCore,
                    twitterCore.getSSLSocketFactory(),
                    executorService, interceptor);
        }
    }

}
