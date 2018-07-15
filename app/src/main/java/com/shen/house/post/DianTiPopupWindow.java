package com.shen.house.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shen.baselibrary.base.BasePopWindow;
import com.shen.house.R;

import org.jetbrains.annotations.Nullable;

public class DianTiPopupWindow extends BasePopWindow {
    public DianTiPopupWindow(Context context) {
        super(context);
    }

    private DianTiCallBack dianTiCallBack;

    public DianTiPopupWindow setDianTiCallBack(DianTiCallBack dianTiCallBack) {
        this.dianTiCallBack = dianTiCallBack;
        return this;
    }

    @Override
    protected View getPopupView() {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dianti_popup, null);
        view.findViewById(R.id.btn_nega).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_posi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dianTiCallBack != null) {
                    CheckBox hasDianTi = view.findViewById(R.id.hasDianTi);
                    RadioGroup rgTi = view.findViewById(R.id.rgTi);
                    RadioGroup rgHu = view.findViewById(R.id.rgHu);
                    int ti, hu;
                    String tis = "", hus = "";

                    int checkedIdTi = rgTi.getCheckedRadioButtonId();
                    switch (checkedIdTi) {
                        case R.id.rbTi1:
                            ti = 1;
                            break;
                        case R.id.rbTi2:
                            ti = 2;
                            break;
                        case R.id.rbTi3:
                            ti = 3;
                            break;
                        case R.id.rbTi4:
                            ti = 4;
                            break;
                        default:
                            ti = -1;
                            break;
                    }
                    View viewRbTi = rgTi.findViewById(checkedIdTi);
                    if (viewRbTi != null) {
                        tis = ((RadioButton) viewRbTi).getText().toString();
                    }

                    int checkedIdHu = rgHu.getCheckedRadioButtonId();
                    switch (checkedIdHu) {
                        case R.id.rbHu1:
                            hu = 1;
                            break;
                        case R.id.rbHu2:
                            hu = 2;
                            break;
                        case R.id.rbHu3:
                            hu = 3;
                            break;
                        case R.id.rbHu4:
                            hu = 4;
                            break;
                        case R.id.rbHu5:
                            hu = 5;
                            break;
                        case R.id.rbHu6:
                            hu = 6;
                            break;
                        default:
                            hu = -1;
                            break;
                    }
                    View viewRbHu = rgHu.findViewById(checkedIdHu);
                    if (viewRbHu != null) {
                        hus = ((RadioButton) viewRbHu).getText().toString();
                    }

                    dianTiCallBack.call(hasDianTi.isChecked(), ti, tis, hu, hus);
                    dismiss();
                }
            }
        });
        return view;
    }

    public DianTiPopupWindow setData(@Nullable PostBean.DianTi dianti) {
        if (dianti != null) {
            View v = getContentView();
            ((CheckBox) v.findViewById(R.id.hasDianTi)).setChecked(dianti.has);
            RadioGroup rgTi = v.findViewById(R.id.rgTi);
            int resId1 = mContext.getResources().getIdentifier("rbTi" + dianti.ti, "id", mContext.getPackageName());
            rgTi.check(resId1);
            RadioGroup rgHu = v.findViewById(R.id.rgHu);
            int resId2 = mContext.getResources().getIdentifier("rbHu" + dianti.hu, "id", mContext.getPackageName());
            rgHu.check(resId2);
        }
        return this;
    }

    public interface DianTiCallBack {
        void call(boolean hasDianTi, int ti, String tis, int hu, String hus);
    }
}
