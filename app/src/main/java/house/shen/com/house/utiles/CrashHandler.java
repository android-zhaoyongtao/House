package house.shen.com.house.utiles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import house.shen.com.house.BuildConfig;
import house.shen.com.house.ContextHouse;


public class CrashHandler extends Instrumentation {
    private static final String TAG = "Instrumentation";
    public Instrumentation instrumentation;

    public static void crashTrack(final Context base) {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");//获取主线程对象，Acitivity 生命周期回调方法crash拦截
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThreadObject = currentActivityThread.invoke(null);
            Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation"); //获取Instrumentation字段
            mInstrumentation.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(activityThreadObject);
            CrashHandler customInstrumentation = new CrashHandler();
            customInstrumentation.instrumentation = instrumentation;
            mInstrumentation.set(activityThreadObject, customInstrumentation);//替换掉原来的,就是把系统的instrumentation替换为自己的Instrumentation对象
        } catch (Throwable e) {
            onCrash(e);
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {//主线程crash拦截
                    try {
                        Looper.loop();//主线程的异常会从这里抛出
                    } catch (Throwable e) {
                        onCrash(e);
                    }
                }
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {//子线程crash拦截
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                onCrash(e);
            }
        });
        if (BuildConfig.DEBUG) {
            SensorManager mSensorManager = ((SensorManager) base.getSystemService(Context.SENSOR_SERVICE));
            final Sensor defaultSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (defaultSensor != null) {
                mSensorManager.registerListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        float value = 0;
                        for (int i = 0; i < event.values.length; i++) {
                            value += (event.values[i] * event.values[i]);
                        }
                        if (value > 2000) {
                            try {
                                Activity activity = ActivityStackManager.getInstance().currentActivity();
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), System.currentTimeMillis() + ".jpg");

                                if (activity != null) {
                                    View decorView = activity.getWindow().getDecorView();
                                    decorView.setDrawingCacheEnabled(true);
                                    Bitmap drawingCache = decorView.getDrawingCache();
                                    FileOutputStream fos = new FileOutputStream(file);
                                    drawingCache.compress(Bitmap.CompressFormat.JPEG, 85, fos);
                                    fos.close();
                                    decorView.destroyDrawingCache();
                                }
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("text/plain"); //纯文本
                                intent.putExtra(Intent.EXTRA_TEXT, new JSONObject().put("title", activity == null ? "无页面展示" : activity.getClass().getSimpleName())
                                        .put("version", ApkUtils.getVersionName(base).replaceAll("\\D", ""))
                                        .put("pic", file.getAbsolutePath())
                                        .put("desc", ActivityLifecycle.mLogList + "\n" + HttpRequest.request + HttpRequest.response + "\n\n\n" + getDeviceInfo()).toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                base.startActivity(intent);
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                }, defaultSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    private static String getDeviceInfo() {
        Field[] fields = Build.class.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append(field.getName()).append("=").append(field.get(null).toString()).append("\n");
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    private static void onCrash(Throwable e) {
        try {
            Activity activity = ActivityStackManager.getInstance().currentActivity();
            if (BuildConfig.DEBUG) {
                LogUtils.e("FATAL", e);
                ToastUtile.showToast("捕获到一个 crash，请使用 qq 等工具分享给研发");
                String traceString = Log.getStackTraceString(e);
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash" + new Date().toString() + ".txt");
                fos.write(traceString.getBytes());
                fos.close();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain"); //纯文本
                intent.putExtra(Intent.EXTRA_TEXT, new JSONObject().put("title", "crash " + activity.getClass().getSimpleName())
                        .put("version", ApkUtils.getVersionName(activity).replaceAll("\\D", ""))
                        .put("desc", traceString + "\n\n\n" + getDeviceInfo()).toString());
                activity.startActivity(intent);
                ActivityStackManager.getInstance().AppExit(ContextHouse.getContext());
            } else {
//                MobclickAgent.reportError(ContextXin.getContext(), e);
            }
        } catch (Throwable e1) {
        }
    }

    @Override
    public void onCreate(Bundle arguments) {
        instrumentation.onCreate(arguments);
    }

    @Override
    public void start() {
        instrumentation.start();
    }

    @Override
    public void onStart() {
        instrumentation.onStart();
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        return true;
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        instrumentation.sendStatus(resultCode, results);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        instrumentation.finish(resultCode, results);
    }

    @Override
    public void setAutomaticPerformanceSnapshots() {
        instrumentation.setAutomaticPerformanceSnapshots();
    }

    @Override
    public void startPerformanceSnapshot() {
        instrumentation.startPerformanceSnapshot();
    }

    @Override
    public void endPerformanceSnapshot() {
        instrumentation.endPerformanceSnapshot();
    }

    @Override
    public void onDestroy() {
        instrumentation.onDestroy();
    }

    @Override
    public Context getContext() {
        return instrumentation.getContext();
    }

    @Override
    public ComponentName getComponentName() {
        return instrumentation.getComponentName();
    }

    @Override
    public Context getTargetContext() {
        return instrumentation.getTargetContext();
    }

    @Override
    public boolean isProfiling() {
        return instrumentation.isProfiling();
    }

    @Override
    public void startProfiling() {
        instrumentation.startProfiling();
    }

    @Override
    public void stopProfiling() {
        instrumentation.stopProfiling();
    }

    @Override
    public void setInTouchMode(boolean inTouch) {
        instrumentation.setInTouchMode(inTouch);
    }

    @Override
    public void waitForIdle(Runnable recipient) {
        instrumentation.waitForIdle(recipient);
    }

    @Override
    public void waitForIdleSync() {
        instrumentation.waitForIdleSync();
    }

    @Override
    public void runOnMainSync(Runnable runner) {
        instrumentation.runOnMainSync(runner);
    }

    @Override
    public Activity startActivitySync(Intent intent) {
        return instrumentation.startActivitySync(intent);
    }

    @Override
    public void addMonitor(ActivityMonitor monitor) {
        instrumentation.addMonitor(monitor);
    }

    @Override
    public ActivityMonitor addMonitor(IntentFilter filter, ActivityResult result, boolean block) {
        return instrumentation.addMonitor(filter, result, block);
    }

    @Override
    public ActivityMonitor addMonitor(String cls, ActivityResult result, boolean block) {
        return instrumentation.addMonitor(cls, result, block);
    }

    @Override
    public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
        return instrumentation.checkMonitorHit(monitor, minHits);
    }

    @Override
    public Activity waitForMonitor(ActivityMonitor monitor) {
        return instrumentation.waitForMonitor(monitor);
    }

    @Override
    public Activity waitForMonitorWithTimeout(ActivityMonitor monitor, long timeOut) {
        return instrumentation.waitForMonitorWithTimeout(monitor, timeOut);
    }

    @Override
    public void removeMonitor(ActivityMonitor monitor) {
        instrumentation.removeMonitor(monitor);
    }

    @Override
    public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
        return instrumentation.invokeMenuActionSync(targetActivity, id, flag);
    }

    @Override
    public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
        return instrumentation.invokeContextMenuAction(targetActivity, id, flag);
    }

    @Override
    public void sendStringSync(String text) {
        instrumentation.sendStringSync(text);
    }

    @Override
    public void sendKeySync(KeyEvent event) {
        instrumentation.sendKeySync(event);
    }

    @Override
    public void sendKeyDownUpSync(int key) {
        instrumentation.sendKeyDownUpSync(key);
    }

    @Override
    public void sendCharacterSync(int keyCode) {
        instrumentation.sendCharacterSync(keyCode);
    }

    @Override
    public void sendPointerSync(MotionEvent event) {
        instrumentation.sendPointerSync(event);
    }

    @Override
    public void sendTrackballEventSync(MotionEvent event) {
        instrumentation.sendTrackballEventSync(event);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return instrumentation.newApplication(cl, className, context);
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        instrumentation.callApplicationOnCreate(app);
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        return instrumentation.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return instrumentation.newActivity(cl, className, intent);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        try {
            instrumentation.callActivityOnCreate(activity, icicle);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        try {
            instrumentation.callActivityOnCreate(activity, icicle, persistentState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnDestroy(Activity activity) {
        try {
            instrumentation.callActivityOnDestroy(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        try {
            instrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        try {
            instrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
        try {
            instrumentation.callActivityOnPostCreate(activity, icicle);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        try {
            instrumentation.callActivityOnPostCreate(activity, icicle, persistentState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        try {
            instrumentation.callActivityOnNewIntent(activity, intent);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnStart(Activity activity) {
        try {
            instrumentation.callActivityOnStart(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnRestart(Activity activity) {
        try {
            instrumentation.callActivityOnRestart(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        try {
            instrumentation.callActivityOnResume(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnStop(Activity activity) {
        try {
            instrumentation.callActivityOnStop(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        try {
            instrumentation.callActivityOnSaveInstanceState(activity, outState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
        try {
            instrumentation.callActivityOnSaveInstanceState(activity, outState, outPersistentState);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        try {
            instrumentation.callActivityOnPause(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    public void callActivityOnUserLeaving(Activity activity) {
        try {
            instrumentation.callActivityOnUserLeaving(activity);
        } catch (Throwable e) {
            onCrash(e);
        }
    }

    @Override
    @Deprecated
    public void startAllocCounting() {
        instrumentation.startAllocCounting();
    }

    @Override
    @Deprecated
    public void stopAllocCounting() {
        instrumentation.stopAllocCounting();
    }

    @Override
    public Bundle getAllocCounts() {
        return instrumentation.getAllocCounts();
    }

    @Override
    public Bundle getBinderCounts() {
        return instrumentation.getBinderCounts();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public UiAutomation getUiAutomation() {
        return instrumentation.getUiAutomation();
    }
}