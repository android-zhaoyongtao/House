package com.shen.baselibrary.utiles;

import android.util.Log;

import com.shen.baselibrary.ContextHouse;


/**
 * 智能Log
 *
 * @author zoudong
 * 打开Log    -------->  adb shell setprop log.tag.url ERROR|DEBUG|WRAN|INFO|VERBOSE
 * 关闭Log    -------->  adb shell setprop log.tag.url false
 */
public class LogUtils {

    public static void e(String tag, String msg) {
        if (Log.isLoggable(tag, Log.ERROR) || ContextHouse.DEBUG) {
            Log.e(tag, msg == null ? "null" : msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Log.isLoggable(tag, Log.DEBUG) || ContextHouse.DEBUG) {
            Log.d(tag, msg == null ? "null" : msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Log.isLoggable(tag, Log.WARN) || ContextHouse.DEBUG) {
            Log.w(tag, msg == null ? "null" : msg);
        }
    }

    public static void i(String tag, String msg) {
        if (ContextHouse.DEBUG) {
            Log.i(tag, msg == null ? "null" : msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Log.isLoggable(tag, Log.VERBOSE) || ContextHouse.DEBUG) {
            Log.v(tag, msg == null ? "null" : msg);
        }
    }

    public static void e(String tag, Throwable tr) {
        if (Log.isLoggable(tag, Log.VERBOSE) || ContextHouse.DEBUG) {
            Log.e(tag, "", tr == null ? new Exception("空") : tr);
        }
    }
}
