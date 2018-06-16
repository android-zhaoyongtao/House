package com.shen.baselibrary.utiles;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shen.baselibrary.ContextHouse;

/**
 * @author zoudong
 */
public class InputTools {
    public static void toggleKeyBoard(boolean b, View v) {
        InputMethodManager imm = (InputMethodManager) ContextHouse.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            imm.showSoftInput(v, 0);
        } else {
//           if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//           }
        }

    }
//    //隐藏虚拟键盘
//    public static void HideKeyboard(View v) {
//        if (v == null) return;
//        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//        }
//    }
//
//    //显示虚拟键盘
//    public static void ShowKeyboard(View v) {
//        if (v == null) return;
//        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
//
//    }
//
//    //强制显示或者关闭系统键盘
//    public static void KeyBoard(final EditText txtSearchKey, final String status) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                InputMethodManager m = (InputMethodManager) txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (status.equals("open")) {
//                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
//                } else {
//                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
//                }
//            }
//        }, 300);
//    }
//
//    /**
//     * 切换软键盘状态
//     *
//     * @param context
//     */
//    public static void toggleKeyBoard(Context context, boolean status, final View view) {
//        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (status) {
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_FORCED);
//        } else {
//            if (view != null) {
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//        }
//    }
//
//    //通过定时器强制隐藏虚拟键盘
//    public static void TimerHideKeyboard(final View v) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm.isActive()) {
//                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                }
//            }
//        }, 10);
//    }
//
//    //输入法是否显示着
//    public static boolean KeyBoard(EditText edittext) {
//        boolean bool = false;
//        InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            bool = true;
//        }
//        return bool;
//
//    }
//
//    /**
//     * 点击其他地方 输入法隐藏
//     *
//     * @param activity
//     * @param ev
//     * @param ignore
//     */
//    public static void dispatchTouchEvent(Activity activity, MotionEvent ev, View ignore) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = activity.getCurrentFocus();
//            if (!isMotionEventView(ignore, ev)) {
//                toggleKeyBoard(activity, false, v);
//                if (v != null) {
//                    v.clearFocus();
//                }
//
//            }
//        }
//    }
//
//    public static void dismissInputKeyBoard(final View view) {
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                toggleKeyBoard(v.getContext(), false, v);
//                return false;
//            }
//        });
//    }
//
//    /**
//     * MotionEvent 点击的区域是否在View 上
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    private static boolean isMotionEventView(View v, MotionEvent event) {
//        if (v != null) {
//            int[] l = {0, 0};
//            v.getLocationInWindow(l);
//            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
//                    + v.getWidth();
//            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
//                // 点击V的事件，忽略它。
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 多种隐藏软件盘方法的其中一种
//     */
//    public static void hideSoftInput(View view) {
//        if (view != null) {
//            IBinder windowToken = view.getWindowToken();
//            if (windowToken != null) {
//                InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//
//    }
}
