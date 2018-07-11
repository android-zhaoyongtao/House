package com.shen.house.post;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 点击TextInputLayout可以跳过内部控件touch事件
 */
public class ClickTextInputLayout extends TextInputLayout {
    public ClickTextInputLayout(Context context) {
        super(context);
    }

    public ClickTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
