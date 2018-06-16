package com.shen.baselibrary.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.shen.baselibrary.R;
import com.shen.baselibrary.utiles.InputTools;
import com.shen.baselibrary.utiles.SlideBackHelper;


public abstract class BaseActivity extends AppCompatActivity implements StatusHandle, View.OnClickListener {

    protected ContentViewWrap mStatusHandler;
    private SlideBackHelper mSlideBackHelper = new SlideBackHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//final 新版本听云要去掉
//        int flag = getIntent().getIntExtra(Key.IntentKey.WINDOW_FLAG, -1);
//        int mask = getIntent().getIntExtra(Key.IntentKey.WINDOW_MASK, -1);
//        if (flag != -1 && mask != -1) {
//            this.getWindow().setFlags(flag, mask);//去掉信息栏
//        }
        beforeSuper(savedInstanceState);
        super.onCreate(savedInstanceState);
//        LogUtils.i("Activity", getClass().getName() + "  " + flag + "  " + mask);
        View view = getContentView(getcontentView());
        if (view.getBackground() == null) {
            view.setBackgroundColor(Color.WHITE);
        }
        setContentView(view);
        mStatusHandler = injectStatus(view);
        injectView(view);
        afterInjectView(view);
        // 【友盟推送】 统计应用启动数据
        //        PushAgent.getInstance(this).onAppStart();
    }

    public @NonNull
    View getContentView(int layout) {
        return LayoutInflater.from(this).inflate(layout, null);
    }

    public ContentViewWrap injectStatus(View view) {
        ContentViewWrap viewWrap = new ContentViewWrap(this);
        viewWrap.setShadow(getResources().getDimensionPixelOffset(R.dimen.title_hight));
        return viewWrap;
    }

    public void injectView(View contentView) {
    }

    public void beforeSuper(Bundle savedInstanceState) {
    }

    /**
     * 侧滑返回的 监听事件
     *
     * @param state
     * @param scrollPercent
     */
    protected void onSwipeScrollStateChange(int state, float scrollPercent) {
    }

    public abstract int getcontentView();

    public abstract void afterInjectView(@NonNull View view);

    /**
     * 显示 dialog 如果0可以取消,点击
     *
     * @param type 可以传值控制是否可以取消和点击下面的界面,如 不可点击并不可取消 DialogHandle.TYPE_UNCANCLE | DialogHandle.TYPE_UNCLICK
     */
    public void onLoading(int type) {
        onLoading(type, null);
    }

    public BaseActivity getThis() {
        return this;
    }

    /**
     * 显示 dialog 如果0可以取消,点击
     *
     * @param type 可以传值控制是否可以取消和点击下面的界面,如 不可点击并不可取消 StatusHandle.TYPE_UNCANCLE | DialogHandle.TYPE_UNCLICK
     * @param desc 显示的提示信息
     */
    public void onLoading(int type, String desc) {
        if (mStatusHandler != null) {
            mStatusHandler.onLoading(type, desc);
        }
    }

    public final void onLoadingFinish(boolean dismissAll) {
        if (mStatusHandler != null) {
            mStatusHandler.onLoadingFinish(dismissAll);
        }
    }

    /**
     * 如果子类要展示 onNoData,onNoNetwork,onError 界面,请实现此方法并传入OnClickListener
     *
     * @param l 当点击 onNoData,onNoNetwork,onError 界面是回调
     */
    @Override
    public void clickError(View.OnClickListener l) {
        if (mStatusHandler != null) {
            mStatusHandler.clickError(l);
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


    public final void onNoData() {
        if (mStatusHandler != null) {
            mStatusHandler.onNoData();
        }
    }

    public final void onNoNetwork() {
        if (mStatusHandler != null) {
            mStatusHandler.onNoNetwork();
        }
    }

    public final void onError() {
        if (mStatusHandler != null) {
            mStatusHandler.onError();
        }
    }

    @Override
    public void onServerError() {
        if (mStatusHandler != null) {
            mStatusHandler.onServerError();
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

    @Override
    public void onClick(@NonNull View v) {
    }

    @Override
    public void onBackPressed() {//返回切页面的动画
        if (((mStatusHandler == null ? TYPE_NORMAL : mStatusHandler.getStatusType()) & TYPE_UNBACK) == TYPE_UNBACK) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            InputTools.toggleKeyBoard(false, getWindow().getDecorView());
            try {//IllegalStateException: Can not perform this action after onSaveInstanceState,  FragmentManagerImpl中checkStateLoss 判断了如果 mStateSaved 是true 就抛出异常，当 stop 的时候就是 true
                super.onBackPressed();
            } catch (Exception e) {
                finish();
            }
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else {
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        BaseActivity.super.onBackPressed();
                    } catch (Exception e) {
                        finish();
                    }
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        InputTools.toggleKeyBoard(false, getWindow().getDecorView());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        InputTools.toggleKeyBoard(false, getWindow().getDecorView());
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//隐藏输入法
        try {// pointerIndex out of range  android.view.MotionEvent.nativeGetAxisValue(Native Method)
            boolean slide = mSlideBackHelper.swipeBack(this, ev);
            boolean superDis = false;
            if (!mSlideBackHelper.isSliding()) {
                superDis = super.dispatchTouchEvent(ev);
            }
            if (!superDis) {//如果子 view 没有处理事件，在判断是否需要滑动
                final View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    InputTools.toggleKeyBoard(false, currentFocus);
                }
            }
            return slide | superDis;
        } catch (Exception e) {
            return true;
        }
    }

    public void setEnableSwipeBack(int size) {
        mSlideBackHelper.setEdgeEffect(size);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkGo.getInstance().cancelTag(this);
    }
}
