package com.raghu.testfire.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtils {

    public static void set(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        switch (object.getClass().getSimpleName()) {
            case "String":
                edit.putString(key, (String) object);
                break;
            case "Integer":
                edit.putInt(key, (Integer) object);
                break;
            case "Boolean":
                edit.putBoolean(key, (Boolean) object);
                break;
            case "Float":
                edit.putFloat(key, (Float) object);
                break;
            case "Long":
                edit.putLong(key, (Long) object);
                break;
        }
        edit.apply();
    }

    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        switch (defaultObject.getClass().getSimpleName()) {
            case "String":
                return sp.getString(key, (String) defaultObject);
            case "Integer":
                return sp.getInt(key, (Integer) defaultObject);
            case "Boolean":
                return sp.getBoolean(key, (Boolean) defaultObject);
            case "Float":
                return sp.getFloat(key, (Float) defaultObject);
            case "Long":
                return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void cleanAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
