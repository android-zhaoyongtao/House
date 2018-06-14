package house.shen.com.house;

import android.content.Context;
import android.graphics.Rect;

import house.shen.com.house.utiles.DisplayUtils;


public class ContextHouse {
    public static float DP1;
    public static String NB_IMEI;
    public static String IMEI;
    public static String MAC;
    public static String CHANNEL;
    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;
    public static Rect CONTENTRECT;
    private static Context context;
//    public static boolean DEBUG;

    public static Context getContext() {
        return context;
    }

    public static void init(Context context) {
        ContextHouse.context = context;
        DP1 = context.getResources().getDisplayMetrics().density;
//        NB_IMEI = ApiKeyUtils.getNB(context);
//        IMEI = SystemUtils.getIMEI(context);
//        MAC = SystemUtils.getLocalMacAddressFromWifiInfo(context);
//        CHANNEL = ApkUtils.getChannel(context);
        SCREENWIDTH = DisplayUtils.getScreenWidth(context);
        SCREENHEIGHT = DisplayUtils.getScreenHeight(context);
        CONTENTRECT = DisplayUtils.getContentViewRect(context);
    }
}
