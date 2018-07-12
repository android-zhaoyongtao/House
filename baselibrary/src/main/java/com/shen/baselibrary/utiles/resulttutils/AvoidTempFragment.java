package com.shen.baselibrary.utiles.resulttutils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.shen.baselibrary.R;

@SuppressLint("ValidFragment")
class AvoidTempFragment extends Fragment {
    private static final String FRAGMENT_TAG = "PermissionUtils";
    private SparseArray<PermissionCallBack> mPermissionCallbacks = new SparseArray<>(1);

    public AvoidTempFragment() {
    }

    public static AvoidTempFragment getInstance(FragmentManager fragmentManager) {
        AvoidTempFragment avoidTempFragment = (AvoidTempFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (avoidTempFragment == null) {
            avoidTempFragment = new AvoidTempFragment();
            fragmentManager
                    .beginTransaction()
                    .add(avoidTempFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return avoidTempFragment;
    }

    public void requestPermission(String[] permissions, PermissionCallBack callback) {
        int requestCode = callback.hashCode();
        mPermissionCallbacks.put(requestCode, callback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionCallBack permissionCallBack = mPermissionCallbacks.get(requestCode);
        if (permissionCallBack != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//成功
                permissionCallBack.hasPermission();
//                LogUtils.e("zhaoy", "有权限");
            } else {//失败
                if (permissions != null && permissions.length > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
//                        LogUtils.e("zhaoy", "拒绝了");
                        permissionCallBack.refusePermission();
                    } else {
//                        LogUtils.e("zhaoy", "2拒绝还勾选了");
                        permissionCallBack.refusePermissionDonotAskAgain();
                    }
                }

            }
            mPermissionCallbacks.remove(requestCode);
        }
    }

    //////////////////////////以下是startActivityForResult()部分/////////////////////////////////////////////////////////////
    private SparseArray<ResultCallback> mRequestActivityCallbacks = new SparseArray<>(1);

    public void startForResult(Intent intent, ResultCallback callback) {
        int requestCode = callback.hashCode();
        mRequestActivityCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ResultCallback callback = mRequestActivityCallbacks.get(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
            mRequestActivityCallbacks.remove(requestCode);
        }
    }
}