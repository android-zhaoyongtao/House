package com.shen.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.utiles.KeyBoardUtils;
import com.shen.baselibrary.utiles.ViewUtils;


public abstract class BasePopWindow2 extends PopupWindow {
    protected Context mContext;

    public BasePopWindow2(final Context context) {
        mContext = context;
        setContentView(getPopupView());
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00));
    }

    @Override
    final public void dismiss() {
        ViewUtils.backgroundAlpha((Activity) mContext, 1f);
        super.dismiss();
    }

    @Override
    public final void setContentView(View contentView) {
        super.setContentView(contentView);
    }

    protected abstract View getPopupView();


    final public BasePopWindow2 showPopupWindow(View parent, int width) {
        KeyBoardUtils.INSTANCE.toggleKeyBoard(false, parent);
        if (!this.isShowing()) {
            makeContentViewWidthExactly(width);
            setWidth(width);
            setHeight(LayoutParams.WRAP_CONTENT);
            //////////↓以下解决华为手机popup不会自动往上移动bug
            Rect r = new Rect();
            parent.getGlobalVisibleRect(r);
            int bottom = r.bottom;
//            LogUtils.e("zhaoy", "rect1:" + r.toShortString());
            int marginBottom = ContextHouse.SCREENHEIGHT - bottom;//parent到屏幕底部距离

            getContentView().measure(0, 0);
            int contentHeight = getContentView().getMeasuredHeight();//展示view的高度，不知道为啥总多202
            if (marginBottom < contentHeight) {//如果下边的高度占不下这个框了
                showAtLocation(parent, Gravity.TOP | Gravity.START, r.left, ContextHouse.SCREENHEIGHT - contentHeight);
//                LogUtils.e("zhaoy","showAtLocation");
//                showAsDropDown(parent,0,-10);
            } else {
                ////////////↑
                showAsDropDown(parent);
//                LogUtils.e("zhaoy","showAsDropDown");
            }

            ViewUtils.backgroundAlpha((Activity) parent.getContext(), 0.5f);
        } else {
            this.dismiss();
        }
        return this;
    }

    final public BasePopWindow2 showPopupWindow(View parent) {
        KeyBoardUtils.INSTANCE.toggleKeyBoard(false, parent);
        if (!this.isShowing()) {
            setWidth(LayoutParams.WRAP_CONTENT);
            setHeight(LayoutParams.WRAP_CONTENT);

            Rect r = new Rect();
            parent.getGlobalVisibleRect(r);
            int bottom = r.bottom;
//            LogUtils.e("zhaoy", "rect1:" + r.toShortString());
            int marginBottom = ContextHouse.SCREENHEIGHT - bottom;//parent到屏幕底部距离

            getContentView().measure(0, 0);
            int contentHeight = getContentView().getMeasuredHeight();//展示view的高度，不知道为啥总多202?
            if (marginBottom < contentHeight) {//如果下边的高度占不下这个框了
                showAtLocation(parent, Gravity.TOP | Gravity.START, r.left, ContextHouse.SCREENHEIGHT - contentHeight);
//                LogUtils.e("zhaoy","showAtLocation");
            } else {
                showAsDropDown(parent);
//                LogUtils.e("zhaoy","showAsDropDown");
            }

            ViewUtils.backgroundAlpha((Activity) parent.getContext(), 0.5f);
        } else {
            this.dismiss();
        }
        return this;
    }

    //确切展示宽度回调
    public void makeContentViewWidthExactly(int width) {
    }
    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
//    private static int getStatusBarHeight(Context context) {
//        int result = 0;
//        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resId > 0) {
//            result = context.getResources().getDimensionPixelOffset(resId);
//        }
//
//        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
//        float h = actionbarSizeTypedArray.getDimension(0, 0);
//        int actionBarHeight = (int) h;
//
//        return result;
//    }
}
