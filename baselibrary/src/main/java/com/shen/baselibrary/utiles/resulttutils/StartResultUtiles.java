package com.shen.baselibrary.utiles.resulttutils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;


/**
 * https://juejin.im/post/5a4611786fb9a0451a76b565
 */
public class StartResultUtiles {

    public static void startForResult(Activity activity, Intent intent, ResultCallback callback) {
        new StartResultUtiles(activity).startForResult(intent, callback);
    }

    public static void startForResult(Fragment fragment, Intent intent, ResultCallback callback) {
        new StartResultUtiles(fragment.getActivity()).startForResult(intent, callback);
    }

    public static void startForResult(Activity activity, Class<?> clazz, ResultCallback callback) {
        new StartResultUtiles(activity).startForResult(clazz, callback);
    }

    public static void startForResult(Fragment fragment, Class<?> clazz, ResultCallback callback) {
        new StartResultUtiles(fragment.getActivity()).startForResult(clazz, callback);
    }


    private AvoidTempFragment mAvoidOnResultFragment;

    private StartResultUtiles(Activity activity) {
        mAvoidOnResultFragment = AvoidTempFragment.getInstance(activity.getFragmentManager());
    }


    private void startForResult(Intent intent, ResultCallback callback) {
        mAvoidOnResultFragment.startForResult(intent, callback.hashCode(), callback);
    }

    private void startForResult(Class<?> clazz, ResultCallback callback) {
        Intent intent = new Intent(mAvoidOnResultFragment.getActivity(), clazz);
        startForResult(intent, callback);
    }
}