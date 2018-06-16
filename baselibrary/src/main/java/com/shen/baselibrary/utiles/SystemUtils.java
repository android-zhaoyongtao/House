package com.shen.baselibrary.utiles;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 获取pad相关类
 */
public class SystemUtils {
    public static final int REQUEST_PERMISS = 325;
    private static Map<String, Integer> mMap = new HashMap<String, Integer>();//权限请求次数
    private static String dex2;//权限请求次数

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL; // 设备型号
    }

    public static String getProcessName(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == Process.myPid()) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                ApplicationInfo appInfo = context.getApplicationInfo();
                String pkg = context.getPackageName();
                int uid = appInfo.uid;
                Class appOpsClass = AppOpsManager.class;
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public static Map<String, String> getDeviceInfo(Context ctx) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                map.put("软件版本名称：", versionName);
                map.put("软件版本号：", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                map.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return map;
    }

    public static boolean checkPermission(String[] permission, final Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> mList = null;
            for (String aPermission : permission) {
                if (act.checkPermission(aPermission, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                    if (mList == null) {
                        mList = new ArrayList<String>();
                    }
                    mList.add(aPermission);
                }
            }
            if (mList != null) {
                act.requestPermissions(mList.toArray(new String[mList.size()]), 1);
                return false;
            }
        }
        return true;
    }

    /**
     * 在 oncreate 和 onRequestPermissionsResult 调用
     *
     * @param hint       如果没有权限的提示文案，如果没有权限需要去设置修改 这个要赋值
     * @param permission 要申请的权限和 hint 一一对应
     * @param act        所在 activity
     * @param back       去设置的对话框点了取消 是否直接调用act的onBackPressed，
     * @return 当调用申请权限 api 和显示去设置对话框时 false，权限都已通过或禁止的权限hint为 null时 true
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean onPermissionResult(String[] hint, String[] permission, final Activity act, final boolean back) {
        ArrayList<String> mList = null;
        for (int i = 0; i < permission.length; i++) {
            if (act.checkPermission(permission[i], Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                Integer times = mMap.get(permission[i]);
                if (times != null && times == 2) {//如果权限请求了2次就直接弹框了，并且把原来的计数清空
                    mMap.remove(permission[i]);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRation = act.shouldShowRequestPermissionRationale(permission[i]);//是否是询问状态
                        if (showRation) {//是询问状态
                            if (mList == null) {
                                mList = new ArrayList<String>();
                            }
                            if (times == null) {
                                mMap.put(permission[i], 1);
                            } else {
                                mMap.put(permission[i], times + 1);
                            }
                            mList.add(permission[i]);//先加到队列里，一会一起申请，如果现在申请的对话框弹出来了  再进到这里申请其他权限会覆盖掉之前的
                            continue;
                        }
                    }//如果走到下面的代码，代表SDK_INT 小于23或者已经禁止调用，显示开启权限的对话框
                }
                if (hint != null && hint[i] != null) {//没有权限的提示文案为 null 不显示提示框      如果先显示去设置开启权限去掉 mList == null &&
                    startPermissSetting(hint[i], act, back);
                    return false;
                }
            }
        }
        if (mList != null) {//
            act.requestPermissions(mList.toArray(new String[mList.size()]), back ? 1 : 2);
            return false;
        }
        return true;//如果上面没 return 代表权限都检测通过
    }

    /**
     * 显示打开权限弹框
     *
     * @param hint 弹框提示
     * @param act  所在 activity
     * @param back 是否返回和关闭程序
     */
    public static void startPermissSetting(String hint, final Activity act, final boolean back) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ToastUtile.showToast(hint);
        } else {
            final AlertDialog alertDialog = new AlertDialog.Builder(act).create();
            alertDialog.setMessage(hint);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    Intent localIntent = new Intent();
                    localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    localIntent.setData(Uri.fromParts("package", act.getPackageName(), null));
                    act.startActivityForResult(localIntent, REQUEST_PERMISS);
                    if (back) {//这里也可以不返回
                        act.finish();
                        ActivityStackManager.getInstance().AppExit(act);
                        Process.killProcess(Process.myPid());//必须要重启进程，否则检测不到开的权限
                    }
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    if (back) {
                        act.finish();
                    }
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    public static String getDex2SHA1(Context context) {
        if (dex2 != null) {
            return dex2;
        }
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return dex2 = a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @SuppressLint("MissingPermission")
    public static void callPhone(Context mContext, String phone) {
        try {
            if (!TextUtils.isEmpty(phone)) {
                Intent it = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                mContext.startActivity(it);
            }
        } catch (Exception e) {
            LogUtils.e("IntentHelper", e);
        }
    }

    public static void callPhone(final Activity activity, final Context act, final String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtile.showToast("无可用电话号码");
        } else {
            callPhone(act, phone);
        }
    }
}
