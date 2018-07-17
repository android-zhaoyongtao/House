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


public abstract class BasePopWindow extends PopupWindow {
    protected Context mContext;

    public BasePopWindow(final Context context) {
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


    final public BasePopWindow showPopupWindow(View targetView, int width) {
        KeyBoardUtils.INSTANCE.toggleKeyBoard(false, targetView);
        if (!this.isShowing()) {
            makeContentViewWidthExactly(width);
            setWidth(width);
            setHeight(LayoutParams.WRAP_CONTENT);
            //////////↓以下解决华为手机popup不会自动往上移动bug
            Rect r = new Rect();
            targetView.getGlobalVisibleRect(r);
            int bottom = r.bottom;//targetView到屏幕顶距离
            int marginBottom = ContextHouse.SCREENHEIGHT - bottom;//targetView到屏幕底部距离

            getContentView().measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), 0);
            int contentHeight = getContentView().getMeasuredHeight();//展示view的高度
            if (marginBottom < contentHeight) {//如果下边的高度占不下这个框了
                showAtLocation(targetView, Gravity.TOP | Gravity.START, r.left, ContextHouse.SCREENHEIGHT - contentHeight);
            } else {
                ////////////↑
                showAsDropDown(targetView);
            }
            ViewUtils.backgroundAlpha((Activity) targetView.getContext(), 0.5f);
        } else {
            this.dismiss();
        }
        return this;
    }

    final public BasePopWindow showPopupWindow(View parent) {
        KeyBoardUtils.INSTANCE.toggleKeyBoard(false, parent);
        if (!this.isShowing()) {
            setWidth(LayoutParams.WRAP_CONTENT);
            setHeight(LayoutParams.WRAP_CONTENT);

            Rect r = new Rect();
            parent.getGlobalVisibleRect(r);
            int bottom = r.bottom;
            int marginBottom = ContextHouse.SCREENHEIGHT - bottom;//parent到屏幕底部距离

            getContentView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
            int contentHeight = getContentView().getMeasuredHeight();//展示view的高度，不知道为啥总多202?
            if (marginBottom < contentHeight) {//如果下边的高度占不下这个框了
                showAtLocation(parent, Gravity.TOP | Gravity.START, r.left, ContextHouse.SCREENHEIGHT - contentHeight);
            } else {
                showAsDropDown(parent);
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
}
