package com.shen.baselibrary.utiles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.shen.baselibrary.ContextHouse;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhaoyt on 16/7/14.
 */
public class ActivityStackManager {
    private static ActivityStackManager instance;
    private Stack<Activity> activityStack;

    private ActivityStackManager() {
        activityStack = new Stack<>();
    }


    public static ActivityStackManager getInstance() {
        if (instance == null) {
            instance = new ActivityStackManager();
        }
        return instance;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isForeground() {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) ContextHouse.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        String appPackageName = ContextHouse.getContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(1);
        if (runningTaskInfo.size() <= 0) {
            return false;
        } else {
            String topAppPackageName = runningTaskInfo.get(0).topActivity.getPackageName();
            return appPackageName.equals(topAppPackageName);
        }
    }

    /**
     * 获取当前Activity
     */
    public int getStackCount() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }

    /**
     * 判断MainActivity 是否打开
     *
     * @return
     */
    public boolean hasMainActivity(Class<? extends Activity> cls) {
        return isContains(cls);
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 返回当前栈顶的activity
     *
     * @return
     */
    public Activity currentActivity() {
        if (activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 栈内是否包含此activity
     *
     * @param cls
     * @return
     */
    public boolean isContains(Class<? extends Activity> cls) {

//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)&&!activity.isFinishing()) {
//                return true;
//            }
//        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity.getClass() == cls && !activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 栈内是否包含此activity的其他实例
     *
     * @param cls
     * @return
     */
    public boolean isContainsCopy(String cls) {

//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)&&!activity.isFinishing()) {
//                return true;
//            }
//        }
        for (int i = activityStack.size() - 2; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().getSimpleName().equals(cls) && !activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 栈内是否包含此activity
     *
     * @param a
     * @return
     */
    public boolean isContains(Activity a) {
        for (Activity activity : activityStack) {
            if (activity.equals(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * activity入栈
     * 一般在baseActivity的onCreate里面加入
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        LogUtils.d("activity~:", "pushActivity: " + activity.getClass().getSimpleName());
    }

    /**
     * 移除栈顶第一个activity
     */
    public void popTopActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * activity出栈
     * 一般在baseActivity的onDestroy里面加入
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            LogUtils.d("activity~:", "popActivity: " + activity.getClass().getSimpleName());
            activityStack.remove(activity);
        }
        if (!activity.isFinishing()) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 主要解决 push 进入activity
     *
     * @param activity
     */
    public void reStartMainActivity(Activity activity) {
//        if (activity.isTaskRoot()) {
//            activity.startActivity(new Intent(activity, MainActivity.class));
//            activity.overridePendingTransition(0, 0);
//        }
    }

    /**
     * activity出栈
     * 一般在baseActivity的onDestroy里面加入
     */
    public void popActivity(Class<?> cls) {
        Activity deleteActivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls) && !activity.isFinishing()) {
                deleteActivity = activity;
                activity.finish();
            }
        }
        activityStack.remove(deleteActivity);
    }

    /**
     * 从栈顶往下移除 直到cls这个activity为止
     * 如： 现有ABCD popAllActivityUntillOne(B.class)
     * 则： 还有AB存在
     * <p>
     * 注意此方法 会把自身也finish掉
     *
     * @param cls
     */
    public void popAllActivityUntillOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 所有的栈元素 除了 cls的留下 其他全部移除
     * 如： 现有ABCD popAllActivityUntillOne(B.class)
     * 则： 只有B存在
     * 注意此方法 会把自身也finish掉
     */
    public void popAllActivityExceptOne(Class cls) {
        Iterator iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (!activity.getClass().equals(cls) && !activity.isFinishing()) {
//                activityStack.remove(activity);
//               注意这里必须要用iterator的remove 上面的则错误
                iterator.remove();
                activity.finish();
            }
        }

    }

    /**
     * 移除所有的activity
     * 退出应用的时候可以调用
     * （非杀死进程）
     */
    public void popAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (null != activity && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            popAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}