package com.shen.baselibrary.utiles.resulttutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;


public class PermissionUtils {

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, @NonNull PermissionCallBack callback) {
        if (hasPermission(context, permission)) {
            callback.hasPermission();
        } else {
            requestPermission((FragmentActivity) context, new String[]{permission}, callback);
        }
    }

    /**
     * 判断权限
     */
    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * 显示前往应用设置
     */
    public static void toAppSetting(final Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    private static void requestPermission(FragmentActivity activity, String[] permissions, PermissionCallBack callback) {
        AvoidTempFragment.getInstance(activity.getSupportFragmentManager()).requestPermission(permissions, callback);
    }

}



