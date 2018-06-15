package house.shen.com.house.utiles;

import android.widget.Toast;

import java.lang.ref.WeakReference;

import house.shen.com.house.ContextHouse;


public class ToastUtile {
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

    public static void showToast(CharSequence tipStr) {
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<>(Toast.makeText(ContextHouse.getContext(), tipStr, Toast.LENGTH_SHORT));
        }
        sToast.get().setText(tipStr);
        sToast.get().show();
    }

    public static void showToast(int tipStr) {
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<>(Toast.makeText(ContextHouse.getContext(), tipStr, Toast.LENGTH_SHORT));
        }
        sToast.get().setText(tipStr);
        sToast.get().show();
    }

}
