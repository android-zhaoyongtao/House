package com.shen.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.shen.baselibrary.utiles.KeyBoardUtils;
import com.shen.baselibrary.utiles.ViewUtils;


public abstract class BasePopWindow extends PopupWindow {
    protected Context mContext;

    public BasePopWindow(final Context context) {
        super(context);
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


    final public BasePopWindow showPopupWindow(View parent, int width) {
        KeyBoardUtils.INSTANCE.toggleKeyBoard(false, parent);
        if (!this.isShowing()) {
            makeContentViewWidthExactly(width);
            setWidth(width);
            setHeight(LayoutParams.WRAP_CONTENT);
            this.showAsDropDown(parent);
            ViewUtils.backgroundAlpha((Activity) parent.getContext(), 0.5f);
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
            this.showAsDropDown(parent);
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
