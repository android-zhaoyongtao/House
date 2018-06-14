package house.shen.com.house.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import house.shen.com.house.R;
import house.shen.com.house.utiles.LogUtils;

public abstract class BaseFragment extends Fragment implements StatusHandle, View.OnClickListener {

    protected Activity mActivity;
    private View root;
    private boolean loadOnce;
    private ContentViewWrap mStatusHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public Activity getThis() {
        return this.mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null) {
                parent.removeView(root);
            }
            return root;
        }
        root = getContentView(getcontentView());
        injectView(root);
        mStatusHandler = injectStatus(root);
        LogUtils.i("BaseFragment", getClass().getName());
        afterInjectView(root);
        if (getUserVisibleHint() && !loadOnce) {
            loadOnce = true;
            getData();
        }
        return root;
    }

    public View getContentView(int layout) {
        View view = View.inflate(mActivity, layout, null);
        FrameLayout frameLayout = new FrameLayout(mActivity);
        frameLayout.addView(view);
        return frameLayout;
    }

    public ContentViewWrap injectStatus(View view) {//只有复写了getContentView 才知道返回的是个什么 view
        ContentViewWrap viewWrap = new ContentViewWrap((FrameLayout) view);
        viewWrap.setShadow(getResources().getDimensionPixelOffset(R.dimen.height_50dp));
        return viewWrap;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (root != null && isVisibleToUser && !loadOnce) {//如果View、可见、没加载过都是 ture 则第一次加载数据
            loadOnce = true;
            getData();
        }
    }

    public void injectView(View contentView) {
//        ButterKnife.bind(this, contentView);
    }


    public abstract int getcontentView();

    public abstract void afterInjectView(View view);

    public void getData() {
    }

    /**
     * 显示 dialog 如果0可以取消,点击
     *
     * @param type 可以传值控制是否可以取消和点击下面的界面,如 不可点击并不可取消 DialogHandle.TYPE_UNCANCLE | DialogHandle.TYPE_UNCLICK
     */
    public void onLoading(int type) {
        onLoading(type, "");
    }

    @Override
    public void clickError(View.OnClickListener l) {
        if (mStatusHandler != null) {
            mStatusHandler.clickError(l);
        }
    }

    @Override
    public void onServerError() {
        if (mStatusHandler != null) {
            mStatusHandler.onServerError();
        }
    }

    /**
     * 如果子类要展示 onNoData,onNoNetwork,onError 界面,请实现此方法并传入OnClickListener
     *
     * @param l 当点击 onNoData,onNoNetwork,onError 界面是回调
     */
    public void clickError(View.OnClickListener l, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        if (mStatusHandler != null) {
            mStatusHandler.clickError(l, marginLeft, marginTop, marginRight, marginBottom);
        }
    }

    /**
     * 显示 dialog 如果0可以取消,点击
     *
     * @param type 可以传值控制是否可以取消和点击下面的界面,如 不可点击并不可取消 StatusHandle.TYPE_UNCANCLE | DialogHandle.TYPE_UNCLICK
     * @param desc 显示的提示信息
     */
    @Override
    public void onLoading(int type, String desc) {
        if (mStatusHandler != null) {
            mStatusHandler.onLoading(type, desc);
        }
    }

    @Override
    public final void onLoadingFinish(boolean dismissAll) {
        if (mStatusHandler != null) {
            mStatusHandler.onLoadingFinish(dismissAll);
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public final void onNoData() {
        if (mStatusHandler != null) {
            mStatusHandler.onNoData();
        }
    }

    @Override
    public final void onNoNetwork() {
        if (mStatusHandler != null) {
            mStatusHandler.onNoNetwork();
        }
    }

    public void setNoData(View noData) {
        if (mStatusHandler != null) {
            mStatusHandler.setNoData(noData);
        }
    }

    public void setNoNet(View noNet) {
        if (mStatusHandler != null) {
            mStatusHandler.setNoNet(noNet);
        }
    }

    public void setError(View error) {
        if (mStatusHandler != null) {
            mStatusHandler.setError(error);
        }
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());       //统计时长
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public final void onError() {
        if (mStatusHandler != null) {
            mStatusHandler.onError();
        }
    }

}
