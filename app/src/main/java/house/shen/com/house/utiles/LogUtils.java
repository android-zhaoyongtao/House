package house.shen.com.house.utiles;

import android.util.Log;

import house.shen.com.house.BuildConfig;


/**
 * 智能Log
 *
 * @author zoudong
 * 打开Log    -------->  adb shell setprop log.tag.url ERROR|DEBUG|WRAN|INFO|VERBOSE
 * 关闭Log    -------->  adb shell setprop log.tag.url false
 */
public class LogUtils {

    public static void e(String tag, String msg) {
        if (Log.isLoggable(tag, Log.ERROR) || BuildConfig.DEBUG) {
            Log.e(tag, msg == null ? "null" : msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Log.isLoggable(tag, Log.DEBUG) || BuildConfig.DEBUG) {
            Log.d(tag, msg == null ? "null" : msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Log.isLoggable(tag, Log.WARN) || BuildConfig.DEBUG) {
            Log.w(tag, msg == null ? "null" : msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg == null ? "null" : msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Log.isLoggable(tag, Log.VERBOSE) || BuildConfig.DEBUG) {
            Log.v(tag, msg == null ? "null" : msg);
        }
    }

    public static void e(String tag, Throwable tr) {
        if (Log.isLoggable(tag, Log.VERBOSE) || BuildConfig.DEBUG) {
            Log.e(tag, "", tr == null ? new Exception("空") : tr);
        }
    }
}
