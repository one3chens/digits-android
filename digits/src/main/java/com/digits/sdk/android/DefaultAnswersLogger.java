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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;

/**
 * This class identifies the presence of Answers Kit in the class path and transparently pipes metrics to Answers
 * when present. This is done to avoid having a dependency on Answers and bloating the sdk.
 *
 * We retrieve pointers to the Classes and Methods that are used in the logger implementation.
 *
 * We retrieve the answers instance lazily since Answers.getInstance() expects that Fabric be initialized.
 *
 * Kits are available via Fabric using Fabric.getInstance(Kit.class) or Kit.getInstance() only after Fabric initialization.
 * Postponing Answers.getInstance() helps us construct this logger in the Digits contructor without having to wait for
 * Fabric initialization.
 */
class DefaultAnswersLogger extends DigitsEventLogger {
    final static DefaultAnswersLogger instance;
    final static String TAG = "DefaultAnswersLogger";
    final static String ANSWERS_CLASS_NAME = "com.crashlytics.android.answers.Answers";
    final static String ANSWERS_EVENT_CLASS_NAME = "com.crashlytics.android.answers.AnswersEvent";
    final static String CUSTOM_EVENT_CLASS_NAME = "com.crashlytics.android.answers.CustomEvent";
    final static String ANSWERS_BUILD_CONFIG_CLASS = "com.crashlytics.android.answers.BuildConfig";

    @NonNull
    static final String answersVersion;
    @Nullable
    static final Class answersClass;
    @Nullable
    static final Class answersEventClass;
    @Nullable
    static final Class customEventClass;
    @Nullable
    static final Method getInstanceMethod;
    @Nullable
    static final Method logCustomMethod;
    @Nullable
    static final Method putCustomAttributeMethod;
    @Nullable
    static final Constructor customEventConstructor;

    static{
        answersVersion = getAnswersVersion();

        //Class: Answers
        answersClass = getClazz(answersVersion, ANSWERS_CLASS_NAME);

        //Class: CustomEvent
        customEventClass = getClazz(answersVersion, CUSTOM_EVENT_CLASS_NAME);

        //Class: AnswersEvent
        //We need this since .putCustomAttribute is defined in this base class
        answersEventClass = getClazz(answersVersion, ANSWERS_EVENT_CLASS_NAME);

        //Method: Answers.getInstance
        getInstanceMethod = getMethod(answersVersion, answersClass, "getInstance");

        //Method: answers.logCustom
        logCustomMethod = getMethod(answersVersion, answersClass, "logCustom", customEventClass);

        //Method: customEvent.putCustomAttribute
        putCustomAttributeMethod = getMethod(answersVersion, answersEventClass,
                "putCustomAttribute", String.class, String.class);

        //Method: new CustomEvent(String.class)
        customEventConstructor = getConstructor(customEventClass, String.class);

        instance = new DefaultAnswersLogger();
    }

    private DefaultAnswersLogger() {
        if (answersClass == null) {
            Log.d(TAG, "Install the Fabric Answers Kit to get Digits Metrics." +
                    "See: https://fabric.io/kits/android/answers");
        }
    }

    @Override
    public void loginBegin(DigitsEventDetails details) {
        final Object answersInstance = invokeMethod(answersVersion, getInstanceMethod,
                answersClass);
        final Object customEvent =
                newInstance(answersVersion, customEventConstructor, "Digits Login Start");

        invokeMethod(answersVersion, putCustomAttributeMethod, customEvent, "Language",
                details.language);
        invokeMethod(answersVersion, logCustomMethod, answersInstance, customEvent);
    }

    @Override
    public void loginSuccess(DigitsEventDetails details) {
        final Object answersInstance = invokeMethod(answersVersion, getInstanceMethod,
                answersClass);
        final Object customEvent =
                newInstance(answersVersion, customEventConstructor, "Digits Login Success");

        invokeMethod(answersVersion, putCustomAttributeMethod, customEvent, "Language",
                details.language);
        invokeMethod(answersVersion, putCustomAttributeMethod, customEvent, "Country",
                details.country);
        invokeMethod(answersVersion, logCustomMethod, answersInstance, customEvent);
    }

    @Override
    public void logout(LogoutEventDetails details) {
        final Object answersInstance = invokeMethod(answersVersion, getInstanceMethod,
                answersClass);
        final Object customEvent =
                newInstance(answersVersion, customEventConstructor, "Digits Logout");

        invokeMethod(answersVersion, putCustomAttributeMethod, customEvent, "Language",
                details.language);
        invokeMethod(answersVersion, putCustomAttributeMethod, customEvent, "Country",
                details.country);
        invokeMethod(answersVersion, logCustomMethod, answersInstance, customEvent);
    }

    @NonNull
    private static String getAnswersVersion() {
        final Class answersBuildConfigClass = getClazz("-", ANSWERS_BUILD_CONFIG_CLASS);
        final Object answersVersionName = getField(answersBuildConfigClass, "VERSION_NAME", null);
        final Object answersBuildNumber = getField(answersBuildConfigClass, "BUILD_NUMBER", null);

        return (answersVersionName == null ? "Unknown Answers Version"
                : (String) answersVersionName)
                + (answersBuildNumber == null ? "Unknown Answers Build Number"
                : (String) answersBuildNumber);
    }

    /**
     * Create a new instance from provided by the constructor, passing to it the list of params.
     * @param cons
     * @param params
     * @return
     */
    @Nullable
    private Object newInstance(@Nullable String answersVersion, @Nullable Constructor cons,
                               Object... params){
        Object instance = null;
        if (answersVersion != null && cons != null) {
            try {
                instance = cons.newInstance(params);
            } catch (InstantiationException e) {
                Fabric.getLogger().d(TAG, String.format("InstantiationException while creating "
                        + "instance from constructor: %s in answers version: %s",
                        cons.getName(), answersVersion));
            } catch (IllegalAccessException e) {
                Fabric.getLogger().d(TAG, String.format("IllegalAccessException while creating "
                        + "instance from constructor: %s in answers version: %s",
                        cons.getName(), answersVersion));
            } catch (InvocationTargetException e) {
                Fabric.getLogger().d(TAG, String.format("InvocationTargetException while creating "
                        + "instance from constructor: %s in answers version: %s",
                        cons.getName(), answersVersion));
            }
        }
        return instance;
    }

    /**
     * Invoke a 'method' on the 'instance' passing to it the list of 'params'
     * @param method
     * @param instance
     * @param params
     * @return
     */
    @Nullable
    private Object invokeMethod(@Nullable String answersVersion, @Nullable Method method,
                                @Nullable Object instance, @Nullable Object... params) {
        Object ret = null;
        if (answersVersion != null && method != null && instance != null) {
            try {
                ret = method.invoke(instance, params);
            } catch (IllegalAccessException e) {
                Fabric.getLogger().d(TAG, String.format("IllegalAccessException while invoking "
                        + "method: %s in answers version: %s", method.getName(), answersVersion));
            } catch (InvocationTargetException e) {
                Fabric.getLogger().d(TAG, String.format("InvocationTargetException while invoking "
                        + "method: %s in answers version: %s", method.getName(), answersVersion));
            }
        }
        return ret;
    }

    /**
     * Get a class
     * @param className
     * @return
     */
    @Nullable
    private static Class getClazz(@Nullable String answersVersion, @Nullable String className){
        Class answersEventClass = null;

        if (answersVersion != null && className != null) {
            try {
                answersEventClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                Fabric.getLogger().d(TAG, String.format("%s class not found in answers version %s",
                        className, answersVersion));
            }
        }

        return answersEventClass;
    }

    /**
     * Gets the method for the given class that accepts parameters of the provided types.
     *
     * The methods are retrived by Class and not by Instance. The recommended usage is to
     * retrieve a pointer to a method in a class and invoke it on different instances of the class.
     * For
     * @param clazz
     * @param methodName
     * @param paramTypes
     * @return
     */
    @Nullable
    private static Method getMethod(@Nullable String answersVersion, @Nullable Class clazz,
                             @Nullable String methodName, @Nullable Class... paramTypes) {
        Method putCustomAttributeMethod = null;
        if (answersVersion != null && clazz != null && methodName != null) {
            try {
                putCustomAttributeMethod = clazz.getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                Fabric.getLogger().d(TAG, String.format("%s method not found in answers version %s",
                        methodName, answersVersion));
            }
        }
        return putCustomAttributeMethod;
    }

    /**
     * Gets the constructor for the given class that accepts parameters of the provided types
     * @param clazz
     * @param paramTypes
     * @return constructor
     */
    @Nullable
    private static Constructor getConstructor(@Nullable Class clazz, @Nullable Class... paramTypes){
        Constructor cons = null;
        if (clazz != null) {
            try {
                cons = clazz.getDeclaredConstructor(paramTypes);
            } catch (NoSuchMethodException e) {
                Fabric.getLogger().d(TAG, String.format("No constructor found for class: %s",
                        clazz.getName()));
            }
        }
        return cons;
    }

    /**
     * Gets the field 'fieldName' defined on the 'instance'.
     * For static fields, instance is null
     * @param clazz
     * @param fieldName
     * @param instance
     * @return
     */
    @Nullable
    private static Object getField(@Nullable Class clazz, @Nullable String fieldName,
                            @Nullable Object instance){
        Field field = null;
        Object ret = null;

        if (clazz != null && fieldName != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                if (field != null) {
                    ret = field.get(instance);
                }
            } catch (NoSuchFieldException e) {
                Fabric.getLogger().d(TAG, String.format("NoSuchFieldException while accessing " +
                        "field %s in clazz %s", fieldName, clazz.getName()));
            } catch (IllegalAccessException e) {
                Fabric.getLogger().d(TAG, String.format("IllegalAccessException while accessing" +
                        "field %s in clazz %s", fieldName, clazz.getName()));
            }
        }
        return ret;
    }
}

