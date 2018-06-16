package com.shen.baselibrary.base;

import android.view.View;

/**
 * Created by dsr on 16/7/15.
 */
public interface StatusHandle {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_UNCANCLE = 1 << 1;
    public static final int TYPE_UNBACK = 1 << 2 | 1;
    public static final int TYPE_UNCLICK = 1;

    void clickError(View.OnClickListener l);

    void onLoading(int type, String desc);

    void onLoadingFinish(boolean dismissAll);

    void onNoData();

    void onNoNetwork();

    void onServerError();

    void onError();
}
