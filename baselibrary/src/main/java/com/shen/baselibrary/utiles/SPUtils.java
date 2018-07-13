package com.shen.baselibrary.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.http.Convert;

public class SPUtils {
    private static final String SPNAME = "sp_house";

    /**
     * 保存String
     */
    public static void setString2SP(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = ContextHouse.getContext().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取String
     */
    public static String getStringFromSP(String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        SharedPreferences sp = ContextHouse.getContext().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 保存int
     */
    public static void setInt2SP(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = ContextHouse.getContext().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取int
     */
    public static int getIntFromSP(String key, int defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        SharedPreferences sp = ContextHouse.getContext().getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static <T> void setJsonObject(String key, T t) {
        String json = Convert.toJson(t);
        setString2SP(key, json);
    }

    public static <T> T getJsonObject(String key, Class<T> t) {
        String json = getStringFromSP(key, "");
        try {
            if (!TextUtils.isEmpty(json)) {
                return Convert.fromJson(json, t);
            }
        } catch (JsonSyntaxException e) {
            return null;
        }

        return null;
    }

}
