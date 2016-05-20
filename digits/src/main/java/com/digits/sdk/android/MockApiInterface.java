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

import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;

import java.util.Collections;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.Query;
import retrofit.mime.TypedByteArray;

public class MockApiInterface implements ApiInterface {

    static final long MOCK_USER_ID = 1;


    @Override
    public void account(@Field("phone_number") String phoneNumber,
                        @Field("numeric_pin") String numericPin, Callback<DigitsUser> cb) {
        final DigitsUser user = new DigitsUser(1, "1");
        final Response response = new Response("/1/sdk/account", 200, "ok",
                Collections.<Header>emptyList(), new TypedByteArray("application/json",
                new Gson().toJson(user).getBytes()));
        cb.success(user, response);
    }

    @Override
    public void auth(@Field("x_auth_phone_number") String phoneNumber,
                     @Field("verification_type") String verificationType,
                     @Field("lang") String lang, Callback<AuthResponse> cb) {
        final AuthResponse data = new AuthResponse();
        data.authConfig = new AuthConfig();
        data.authConfig.isVoiceEnabled = true;
        final Response response = new Response("/1/sdk/login", 200, "ok",
                Collections.<Header>emptyList(), new TypedByteArray("application/json",
                new Gson().toJson(data).getBytes()));
        cb.success(data, response);
    }

    @Override
    public void login(@Field("login_verification_request_id") String requestId,
                      @Field("login_verification_user_id") long userId,
                      @Field("login_verification_challenge_response") String code,
                      Callback<DigitsSessionResponse> cb) {
        final DigitsSessionResponse data = new DigitsSessionResponse();
        data.secret = "secret";
        data.token = "token";
        data.userId = MOCK_USER_ID;
        final Response response = new Response("/auth/1/xauth_challenge.json", 200, "ok",
                Collections.<Header>emptyList(), new TypedByteArray("application/json",
                new Gson().toJson(data).getBytes()));
        cb.success(data, response);
    }

    @Override
    public void verifyPin(@Field("login_verification_request_id") String requestId,
                          @Field("login_verification_user_id") long userId,
                          @Field("pin") String pin, Callback<DigitsSessionResponse> cb) {
    }

    @Override
    public void email(@Field("email_address") String email, Callback<DigitsSessionResponse> cb) {
    }

    @Override
    public void verifyAccount(Callback<VerifyAccountResponse> cb) {
    }

    @Override
    public void register(@Field("raw_phone_number") String rawPhoneNumber,
                         @Field("text_key") String textKey,
                         @Field("send_numeric_pin") Boolean sendNumericPin,
                         @Field("lang") String lang, @Field("client_identifier_string") String id,
                         @Field("verification_type") String verificationType,
                         Callback<DeviceRegistrationResponse> cb) {
    }

    @Override
    public UploadResponse upload(@Body Vcards vcards) {
        return null;
    }

    @Override
    public void deleteAll(Callback<Response> cb) {
    }

    @Override
    public void usersAndUploadedBy(@Query("next_cursor") String nextCursor,
                                   @Query("count") Integer count, Callback<Contacts> cb) {
    }
}
