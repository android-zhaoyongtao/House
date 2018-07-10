
package com.shen.house;

import android.app.Application;
import android.content.Context;

import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.utiles.ActivityLifecycle;
import com.shen.baselibrary.utiles.CrashHandler;
import com.shen.baselibrary.utiles.SystemUtils;

public class AppContextApplication extends Application {

//    {
//        //微信
//        PlatformConfig.setWeixin(BuildConfig.wxid, BuildConfig.wxsecret);
//        //新浪微博
////        PlatformConfig.setSinaWeibo(BuildConfig.sinaid, BuildConfig.sinasecret, "http://www.umeng.com/social");
//        //QQ
//        PlatformConfig.setQQZone(BuildConfig.qqid, BuildConfig.qqkey);
//        //        Config.REDIRECT_URL = "http://www.umeng.com/social"; //这个必须和填写的一样  否则报文件不存在C8998
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CrashHandler.crashTrack(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (getPackageName().equals(SystemUtils.getProcessName(this))) {
            ContextHouse.init(this);
//            OkHttpUtils.initClient(new OkHttpClient.Builder().readTimeout(100, TimeUnit.SECONDS).dispatcher(new Dispatcher(ExecutorUtile.threadPoolExecutor)).build());
//            Router.getInstance().addModule(IBaseRentModule.class, new BaseRentImpl(this));
//            Router.getInstance().addModule(IWebModule.class, new WebModuleImpl(this));
//            Router.getInstance().addModule(IShareThirdModule.class, new ShareThirdModule());
//            ContextHouse.DEBUG = BuildConfig.DEBUG && 1 == SPUtils.getIntFromSP("beta", 1);
//            JPushInterface.setDebugMode(BuildConfig.DEBUG);
//            JPushInterface.init(this);// 初始化 JPush 30ms
//            MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, ContextXin.DEBUG ? "57875a8367e58e214d001964" : "577c9510e0f55a61400025c2", ContextXin.CHANNEL));
//            MobclickAgent.openActivityDurationTrack(false);
            registerActivityLifecycleCallbacks(new ActivityLifecycle());

//            StatisManager.getInstance().onTriggerNow(null, StatisticKey.OPEN_APP, "operation", "start");
//            UMShareAPI.get(this);
//            initApm();

        }
    }

//    private void initApm() {
//        IBaseRentModule module = Router.getInstance().getModule(IBaseRentModule.class);
//        CityInfoEntity cityInfo = module.getCityInfo();
//        ApmUpload.getInstance().init(this, true, "0".equals(BuildConfig.CLIENT_ID)?"100001":"100002",new ApmUpload.Config()
//                .setCid(ContextXin.NB_IMEI)
//                .setXdid(FingerPrintManager.getFingerPrintId(this))
//                .setChannel(ContextXin.CHANNEL).setLatitude(cityInfo.latitude+"").setLongitude(cityInfo.longitude+""));
//    }

}
