package com.shen.house.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shen.baselibrary.base.BasePopWindow;
import com.shen.house.R;

public class ShiTingPopupWindow extends BasePopWindow {
    public ShiTingPopupWindow(Context context) {
        super(context);
    }

    private ShiTingCallBack shiTingCallBack;

    public ShiTingPopupWindow setShiTingCallBack(ShiTingCallBack shiTingCallBack) {
        this.shiTingCallBack = shiTingCallBack;
        return this;
    }

    @Override
    protected View getPopupView() {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_shiting_popup, null);
        view.findViewById(R.id.btn_nega).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_posi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shiTingCallBack != null) {

                    RadioGroup rgShi = view.findViewById(R.id.rgShi);
                    RadioGroup rgTing = view.findViewById(R.id.rgTing);
                    RadioGroup rgWei = view.findViewById(R.id.rgWei);
                    int shi, ting, wei;
                    String shis = "", tings = "", weis = "";

                    int checkedIdShi = rgShi.getCheckedRadioButtonId();
                    switch (checkedIdShi) {
                        case R.id.rbShi1:
                            shi = 1;
                            break;
                        case R.id.rbShi2:
                            shi = 2;
                            break;
                        case R.id.rbShi3:
                            shi = 3;
                            break;
                        case R.id.rbShi4:
                            shi = 4;
                            break;
                        default:
                            shi = -1;
                            break;
                    }
                    View viewRbShi = rgShi.findViewById(checkedIdShi);
                    if (viewRbShi != null) {
                        shis = ((RadioButton) viewRbShi).getText().toString();
                    }

                    int checkedIdTing = rgTing.getCheckedRadioButtonId();
                    switch (checkedIdTing) {
                        case R.id.rbTing1:
                            ting = 1;
                            break;
                        case R.id.rbTing2:
                            ting = 2;
                            break;
                        case R.id.rbTing3:
                            ting = 3;
                            break;
                        default:
                            ting = -1;
                            break;
                    }
                    View viewRbTing = rgTing.findViewById(checkedIdTing);
                    if (viewRbTing != null) {
                        tings = ((RadioButton) viewRbTing).getText().toString();
                    }

                    int checkedRbWei = rgWei.getCheckedRadioButtonId();
                    switch (checkedRbWei) {
                        case R.id.rbWei1:
                            wei = 1;
                            break;
                        case R.id.rbWei2:
                            wei = 2;
                            break;
                        case R.id.rbWei3:
                            wei = 3;
                            break;
                        default:
                            wei = -1;
                            break;
                    }
                    View viewRbWei = rgWei.findViewById(checkedRbWei);
                    if (viewRbWei != null) {
                        weis = ((RadioButton) viewRbWei).getText().toString();
                    }
                    shiTingCallBack.call(shi, shis, ting, tings, wei, weis);
                    dismiss();
                }
            }
        });
        return view;
    }

    public interface ShiTingCallBack {
        void call(int shi, String shis, int ting, String tings, int wei, String weis);
    }
}
