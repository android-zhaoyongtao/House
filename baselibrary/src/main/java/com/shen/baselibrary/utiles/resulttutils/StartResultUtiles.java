package com.shen.baselibrary.utiles.resulttutils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


/**
 * https://juejin.im/post/5a4611786fb9a0451a76b565
 */
public class StartResultUtiles {

    public static void startForResult(FragmentActivity activity, Intent intent, ResultCallback callback) {
        AvoidTempFragment.getInstance(activity.getSupportFragmentManager()).startForResult(intent, callback);
    }

    public static void startForResult(Fragment fragment, Intent intent, ResultCallback callback) {
        startForResult(fragment.getActivity(), intent, callback);
    }

    public static void startForResult(FragmentActivity activity, Class<?> clazz, ResultCallback callback) {
        Intent intent = new Intent(activity, clazz);
        startForResult(activity, intent, callback);
    }

    public static void startForResult(Fragment fragment, Class<?> clazz, ResultCallback callback) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        startForResult(fragment, intent, callback);
    }
}