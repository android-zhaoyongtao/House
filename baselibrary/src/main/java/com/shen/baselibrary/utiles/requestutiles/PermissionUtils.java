package com.shen.baselibrary.utiles.requestutiles;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;


public class PermissionUtils {

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, PermissionCallBack callback) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            callback.hasPermission();
        } else {
            new PermissionUtils((Activity) context).requestPermission(new String[]{permission}, callback.hashCode(), callback);
        }
    }

    /**
     * 显示前往应用设置Dialog
     */
    public static void showToAppSettingDialog(final Activity activity, String permissonName) {
        new AlertDialog.Builder(activity)
                .setTitle("需要权限")
                .setMessage("我们需要" + permissonName + "权限，才能实现功能，点击前往，将转到应用的设置界面，请开启相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    private AvoidTempFragment avoidTempFragment;

    private PermissionUtils(Activity activity) {
        avoidTempFragment = AvoidTempFragment.getInstance(activity.getFragmentManager());
    }

    private void requestPermission(String[] permissions, int requestCode, PermissionCallBack callback) {
        avoidTempFragment.requestPermission(permissions, requestCode, callback);
    }

}



