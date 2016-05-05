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

import com.digits.sdk.android.DigitsEventLogger;

import io.fabric.sdk.android.Fabric;

/**
 * This logger does multiple things to demonstrate the flexibility
 * we intend to provide
 * 1) Generate Answers Custom Events
 * 2) Log to stdOut
 */
public class AnswersLogger extends DigitsEventLogger {
    private final Answers answers;
    private final String TAG = "AnswersLogger";

    AnswersLogger(Answers answers) {
        this.answers = answers;
    }

    @Override
    public void phoneNumberImpression() {
        answers.logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberImpression"));

        Fabric.getLogger().d(TAG, "phoneNumberImpression");
    }

    @Override
    public void phoneNumberSubmit() {
        answers.logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberSubmit"));

        Fabric.getLogger().d(TAG, "phoneNumberSubmit");
    }

    @Override
    public void phoneNumberSuccess() {
        answers.logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "phoneNumberSuccess"));

        Fabric.getLogger().d(TAG, "phoneNumberSuccess");
    }

   @Override
   public void loginSuccess() {
        answers.logCustom(new CustomEvent("Login-Digits")
                .putCustomAttribute("Action", "loginSuccess"));

        Fabric.getLogger().d(TAG, "loginSuccess");
    }
}
