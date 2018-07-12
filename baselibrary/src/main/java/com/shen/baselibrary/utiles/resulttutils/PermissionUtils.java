package com.shen.baselibrary.utiles.resulttutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;


public class PermissionUtils {

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, @NonNull PermissionCallBack callback) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            callback.hasPermission();
        } else {
            new PermissionUtils((Activity) context).requestPermission(new String[]{permission}, callback.hashCode(), callback);
        }
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

    private AvoidTempFragment avoidTempFragment;

    private PermissionUtils(Activity activity) {
        avoidTempFragment = AvoidTempFragment.getInstance(activity.getFragmentManager());
    }

    private void requestPermission(String[] permissions, int requestCode, PermissionCallBack callback) {
        avoidTempFragment.requestPermission(permissions, requestCode, callback);
    }

}



