package com.shen.baselibrary.utiles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.util.SparseArray;

import com.shen.baselibrary.R;


/**
 * https://juejin.im/post/5a4611786fb9a0451a76b565
 * 使用示例：
 * //callback方式
 * callback.setOnClickListener {
 * AvoidOnResult(this).startForResult(FetchDataActivity::class.java, REQUEST_CODE_CALLBACK, object : AvoidOnResult.Callback {
 * override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
 * if (resultCode == Activity.RESULT_OK) {
 * val text = data?.getStringExtra("text")
 * Toast.makeText(this@MainActivity, "callback -> " + text, Toast.LENGTH_SHORT).show()
 * } else {
 * Toast.makeText(this@MainActivity, "callback canceled", Toast.LENGTH_SHORT).show()
 * }
 * <p>
 * })
 * }
 */

public class AvoidOnResult {
    private String FRAGMENT_TAG = "AvoidOnResult";
    private AvoidOnResultFragment mAvoidOnResultFragment;

    public AvoidOnResult(Activity activity) {
        mAvoidOnResultFragment = getAvoidOnResultFragment(activity);
    }

//    public AvoidOnResult(Fragment fragment){
//        this(fragment.getActivity());
//    }

    private AvoidOnResultFragment getAvoidOnResultFragment(Activity activity) {
        AvoidOnResultFragment avoidOnResultFragment = findAvoidOnResultFragment(activity);
        if (avoidOnResultFragment == null) {
            avoidOnResultFragment = new AvoidOnResultFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(avoidOnResultFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return avoidOnResultFragment;
    }

    private AvoidOnResultFragment findAvoidOnResultFragment(Activity activity) {
        return (AvoidOnResultFragment) activity.getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    public void startForResult(Intent intent, int requestCode, ResultCallback callback) {
        mAvoidOnResultFragment.startForResult(intent, requestCode, callback);
    }

    public void startForResult(Class<?> clazz, int requestCode, ResultCallback callback) {
        Intent intent = new Intent(mAvoidOnResultFragment.getActivity(), clazz);
        startForResult(intent, requestCode, callback);
    }
}

@SuppressLint("ValidFragment")
class AvoidOnResultFragment extends Fragment {

    private SparseArray<ResultCallback> mCallbacks = new SparseArray<>(2);

    public AvoidOnResultFragment() {
    }

    public void startForResult(Intent intent, int requestCode, ResultCallback callback) {
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ResultCallback callback = mCallbacks.get(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
        mCallbacks.remove(requestCode);
    }

}

interface ResultCallback {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}