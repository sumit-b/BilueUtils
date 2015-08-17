package au.com.woolworthslimited.bilueutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Copyright (c) 2014 Woolworths. All rights reserved.
 */
public class SharedPrefUtils {
    private static final String SHOPPING_MODE = "shoppingMode";
    private static final String KEY_HAS_LISTS_SYNC_PASSED = "has_lists_sync_passed";

    private static final String DEFAULT_LIST = "defaultList";

    private static final String STORE = "store";
    private static final String EDR = "edr";
    private static final String DATA_IMPORTED = "data_imported";
    private static final String TEMP_DEFAULT_LIST = "temp_default_list";

    // General put/get methods
    public static void putInt(String key, int value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defValue, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defValue);
    }

    public static void putString(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }

    public static void remove(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }


}