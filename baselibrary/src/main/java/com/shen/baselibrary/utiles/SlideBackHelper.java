package com.shen.baselibrary.utiles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.R;

import java.util.Stack;

/**
 * 侧滑返回
 */

public class SlideBackHelper {
    private static final String TAG = "SlideBackHelper";
    private int downX;
    private int downY;
    private ImageView backAct;
    private ViewGroup contentView;
    private float edgeEffect = (int) (ContextHouse.DP1 * 20);
    private View conent;

    public boolean swipeBack(final Activity context, MotionEvent event) {
        try {
            Stack<Activity> list = ActivityStackManager.getInstance().getActivityStack();
            if (list.size() > 1) {//最少有两个activity，只剩一个的时候不能滑动
                float x = event.getX();
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {//如果按下的位置在左侧边缘
                    if (edgeEffect > x) {
                        downX = Integer.MIN_VALUE | (int) (x + 1);//防止rawx=0当做没按下处理//downx里包含了两个状态，一个是min_value标识是否滑动超过抖动限制，一个是当前按下的x距离
                        downY = (int) event.getY();
                        Log.i(TAG, downX + "  down " + downY);
                    } else {
                        downX = Integer.MIN_VALUE;
                    }
                } else if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)) {//如果抬起的时候已经滑动屏幕了
                    if (downX >= 0) {//滑动超过抖动
                        final int rootWidth = context.getWindow().getDecorView().getWidth();
                        if ((x - downX > rootWidth / 2 || (x - downX) / (event.getEventTime() - event.getDownTime()) > rootWidth / 1000f)) {//如果滑动距离超过屏幕的一半或者平均1秒能滑过全屏的速度
                            Log.i(TAG, downX + "  upfinish " + downY);
                            ValueAnimator animator = ValueAnimator.ofFloat(x - downX, DisplayUtils.getScreenWidth(context));//关掉activity
                            animator.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    float value = (float) valueAnimator.getAnimatedValue();
                                    slideTo(value, rootWidth);
                                }
                            });
                            animator.start();
                            animator.addListener(new AnimatorListenerAdapter() {

                                @Override
                                public void onAnimationEnd(Animator arg0) {
                                    context.finish();
                                    context.overridePendingTransition(R.anim.dissmis_now, R.anim.show_now);
                                    if (backAct != null) {
                                        backAct.setBackground(null);
                                        backAct = null;
                                        contentView = null;
                                    }//防止 acitivity 泄露导致引用一张大图
                                    if (conent != null) {
                                        conent.destroyDrawingCache();
                                        conent = null;
                                    }
                                }

                            });
                        } else {//恢复到这个acitivity的起始状态
                            Log.i(TAG, downX + "  upreset " + downY);
                            ValueAnimator animator = ValueAnimator.ofFloat(x - downX, 0);
                            animator.setDuration(100).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    float value = (float) valueAnimator.getAnimatedValue();
                                    slideTo(value, rootWidth);
                                }
                            });
                            animator.start();
                            animator.addListener(new AnimatorListenerAdapter() {

                                @Override
                                public void onAnimationEnd(Animator arg0) {
                                    if (backAct != null) {
                                        backAct.setBackground(null);
                                        ((ViewGroup) backAct.getParent()).removeView(backAct);
                                        backAct = null;
                                        contentView = null;
                                    }
                                    if (conent != null) {
                                        conent.destroyDrawingCache();
                                        conent = null;
                                    }
                                }
                            });
                        }
                        event.setAction(MotionEvent.ACTION_CANCEL);
                    } else {//滑动没有超过抖动，按下直接抬起了
                        Log.i(TAG, downX + "  upcancel " + downY);
                        downX = Integer.MIN_VALUE;
                    }
                } else if (action == MotionEvent.ACTION_MOVE && downX != Integer.MIN_VALUE) {
                    if ((downX | Integer.MAX_VALUE) == 0xffffffff) {//如果downx里包含min_value    则   还没有划出 抖动限制
                        int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
                        float moveX = Math.abs(x - (downX & Integer.MAX_VALUE));
                        if (moveX > touchSlop) {//滑动距离超过 抖动限制
                            if (moveX > Math.abs(event.getY() - downY)) {//x轴滑动的多
                                View decor = context.getWindow().getDecorView();
                                if (decor instanceof FrameLayout && ((FrameLayout) decor).getChildCount() > 0) {//直接把底下的activity当做一个view的背景加到和contentview同一层
                                    conent = list.get(list.size() - 2).getWindow().getDecorView();
                                    conent.setDrawingCacheEnabled(true);
                                    Bitmap drawingCache = conent.getDrawingCache();
                                    if (drawingCache != null) {
                                        downX = (downX &= Integer.MAX_VALUE) - 1;//去掉最高位 只留其中x距离的状态  把downx开始+1减掉
                                        contentView = (ViewGroup) ((FrameLayout) decor).getChildAt(0);
                                        backAct = new ImageView(context);
                                        backAct.setScaleType(ImageView.ScaleType.FIT_XY);
                                        ((FrameLayout) decor).addView(backAct, 0);
                                        ViewGroup.LayoutParams params = backAct.getLayoutParams();
                                        params.width = -1;
                                        params.height = -1;
                                        backAct.setImageBitmap(drawingCache);
                                        Log.i(TAG, downX + "  startmove " + downY);
                                    } else {
                                        downX = Integer.MIN_VALUE;
                                        Log.i(TAG, downX + " bitmap=null  " + downY);
                                    }
                                } else {
                                    downX = Integer.MIN_VALUE;
                                    Log.i(TAG, downX + " decorerror  " + downY);
                                }
                                return true;
                            } else {//y轴滑动的多
                                downX = Integer.MIN_VALUE;//本次滑动失效 初始化downx
                                Log.i(TAG, downX + " ymax  " + downY);
                            }
                        }
                    } else {
                        Log.i(TAG, downX + "  slide " + downY);
                        slideTo(x - downX, context.getWindow().getDecorView().getWidth());
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "fatal", e);
        }
        return false;
    }

    private void slideTo(float value, int rootWidth) {
        if (contentView != null && backAct != null) {
            contentView.scrollTo((int) -value, 0);
            backAct.setTranslationX(-(rootWidth - value) / 3);
//        backAct.setScaleX((value) / rootWidth * 0.05f + 0.95f);
//        backAct.setScaleY((value) / rootWidth * 0.05f + 0.95f);
            backAct.setAlpha((value) / rootWidth * 0.3f + 0.7f);
        }
    }

    public boolean isSliding() {
        return downX > 0;
    }

    public void setEdgeEffect(float edgeEffect) {
        this.edgeEffect = edgeEffect;
    }
}
