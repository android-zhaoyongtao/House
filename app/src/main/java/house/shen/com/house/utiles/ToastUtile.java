package house.shen.com.house.utiles;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;


class ToastUtile {
    private static WeakReference<Toast> sToast;
//    private static WeakReference<Toast> lTast;
//
//    public static void showLongToast(Context context, CharSequence tipStr) {
//        if (lTast == null) {
//            lTast = new WeakReference<>(Toast.makeText(context, tipStr, Toast.LENGTH_LONG));
//        }
//        lTast.get().setText(tipStr);
//        lTast.get().show();
//    }

    public static void showToast(Context appContext, CharSequence tipStr) {
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<>(Toast.makeText(appContext, tipStr, Toast.LENGTH_SHORT));
        }
        sToast.get().setText(tipStr);
        sToast.get().show();
    }

    public static void showToast(Context appContext, int tipStr) {
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<>(Toast.makeText(appContext, tipStr, Toast.LENGTH_SHORT));
        }
        sToast.get().setText(tipStr);
        sToast.get().show();
    }

}
