package com.shen.baselibrary.utiles;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shen.baselibrary.ContextHouse;

import java.lang.reflect.Field;

public class ViewUtils {

    //将TabLayout中Indicator宽度设为字符的宽度
    public static void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    int tabCount = tabLayout.getTabCount();
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = (int) ((ContextHouse.SCREENWIDTH / tabCount - width) / 2);
                        params.rightMargin = (int) ((ContextHouse.SCREENWIDTH / tabCount - width) / 2);
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }

                } catch (Exception e) {
//                    Log.e("zoudong-ViewUtiles", "run====" + e.toString());
                }
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        if (context instanceof Activity) {
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            context.getWindow().setAttributes(lp);
        }
    }
}
