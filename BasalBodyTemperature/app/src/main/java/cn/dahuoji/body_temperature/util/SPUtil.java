package cn.dahuoji.body_temperature.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import cn.dahuoji.body_temperature.LocalApplication;

/**
 * Created by 10732 on 2018/5/21.
 */

public class SPUtil {

    //存Cookie用的SP
    private static final String HttpHeaderSPCacheName = "config";

    private final static SharedPreferences sharedPreferences = LocalApplication.getContext().getSharedPreferences(HttpHeaderSPCacheName, Context.MODE_PRIVATE);

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void putString(String name, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getString(String name) {
        return sharedPreferences.getString(name, "");
    }

    public static void putLong(String name, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static long getLong(String name) {
        return sharedPreferences.getLong(name, 0);
    }

    public static void putInt(String name, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int getInt(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    public static int getSortType(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    public static void putBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static boolean getBoolean(String name) {
        return sharedPreferences.getBoolean(name, false);
    }

    public static void putFloat(String name, float value) {
        if (value < 0) return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(name, value);
        editor.apply();
    }

    public static float getFloat(String name) {
        return sharedPreferences.getFloat(name, -1f);
    }
}
