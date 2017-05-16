package com.example.alwayslike.jihuo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.alwayslike.jihuo.Jihuo;


public class SharePreferenceHanler {

    private static final String PREFERENCES_NAME = "SETTINGS";

    /**
     * 写int类型的配置文件
     *
     * @param perferencesName
     * @param key
     * @param value
     */
    public static void writePreferences(String perferencesName, String key,
                                        Object value) {
        SharedPreferences preferences = Jihuo.mContext
                .getSharedPreferences(perferencesName,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String type = value.getClass().toString();
        if (type.equals("class java.lang.Integer"))
            editor.putInt(key, Integer.parseInt(value.toString()));
        else if (type.equals("class java.lang.String"))
            editor.putString(key, value.toString());
        editor.commit();
    }

    public int readIntPreferences(String perferencesName, String key) {
        SharedPreferences preferences = Jihuo.mContext
                .getSharedPreferences(perferencesName,
                        Context.MODE_PRIVATE);
        int result = preferences.getInt(key, 0);
        return result;
    }

    public String readStringPreferences(String perferencesName, String key) {
        SharedPreferences preferences = Jihuo.mContext
                .getSharedPreferences(perferencesName,
                        Context.MODE_PRIVATE);
        String result = preferences.getString(key, "");
        return result;
    }

    public static boolean readBoolean(String key) {
        SharedPreferences preferences = Jihuo.mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static int readInt(String key) {
        SharedPreferences preferences = Jihuo.mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static String readString(String key) {
        SharedPreferences preferences = Jihuo.mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void writePreferences(String key, Object val) {

        if (val == null)
            return;

        SharedPreferences preferences = Jihuo.mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (val instanceof Integer) {
            editor.putInt(key, (Integer) val);
        } else if (val instanceof String) {
            editor.putString(key, (String) val);
        } else if (val instanceof Boolean) {
            editor.putBoolean(key, (Boolean) val);
        }

        editor.commit();

    }

}
