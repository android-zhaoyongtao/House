package house.shen.com.house.utiles;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

import house.shen.com.house.BuildConfig;

//import com.umeng.analytics.MobclickAgent;
//import com.xin.baserent.StatisticKey;
//import com.xin.dbm.statistics.StatisManager;

/**
 * 统计应用切换到前台
 */

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    public static LinkedList<String> mLogList;
    private boolean mForgound = true;//默认值 false 刚进入应用打点， true 刚进入应用不打点

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityStackManager.getInstance().pushActivity(activity);
        if (BuildConfig.DEBUG) {
            if (mLogList == null) {
                mLogList = new LinkedList<String>();
            }
            if (mLogList.size() > 14) {
                mLogList.removeFirst();
            }
            Bundle extras = activity.getIntent().getExtras();
            mLogList.addLast(activity.getClass().getName() + " 参数  " + (extras == null ? "" : extras.toString()));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        if ((activity instanceof FragmentActivity && ((FragmentActivity) activity).getSupportFragmentManager().getFragments() == null)) {
//            MobclickAgent.onPageStart(this.getClass().getSimpleName());
//        }
//        MobclickAgent.onResume(activity);


        if (!mForgound) {
//            StatisManager.getInstance().onTriggerNow(null, StatisticKey.OPEN_APP, "operation", "awake");
//            StatisManager.getInstance().onExitApp("active");
            LogUtils.i("ActivityLifecycle", "house    切换到前台");
            mForgound = true;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        if ((activity instanceof FragmentActivity && ((FragmentActivity) activity).getSupportFragmentManager().getFragments() == null)) {
//            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//        }
//        MobclickAgent.onPause(activity);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mForgound) {
            mForgound = ActivityStackManager.isForeground();
            if (!mForgound) {//应用进入后台
//                StatisManager.getInstance().onExitApp("background");
//                StatisManager.getInstance().onPrepared(null);
                LogUtils.i("ActivityLifecycle", "house    切换到后台");
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStackManager.getInstance().popActivity(activity);
        if (BuildConfig.DEBUG) {
            if (mLogList == null) {
                mLogList = new LinkedList<String>();
            }
            if (mLogList.size() > 14) {
                mLogList.removeFirst();
            }
            mLogList.addLast(activity.getClass().getName() + "关闭");
        }
    }

}
