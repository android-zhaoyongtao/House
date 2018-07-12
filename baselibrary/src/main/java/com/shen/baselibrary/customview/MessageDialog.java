package com.shen.baselibrary.customview;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.R;


/**
 */
public class MessageDialog extends Dialog {
    //一个确定按钮的
    public MessageDialog(Activity activity, String title, String message, String posiText, final View.OnClickListener posi) {
        this(activity, title, message, false, null, null, posiText, posi);
    }

    //两个确定取消按钮的
    public MessageDialog(Activity activity, String title, String message, String negeText, final View.OnClickListener nega, String posiText, final View.OnClickListener posi) {
        this(activity, title, message, true, negeText, nega, posiText, posi);
    }

    private MessageDialog(Activity activity, String title, String message, boolean showNegaButton, String negeText, final View.OnClickListener nega, String posiText, final View.OnClickListener posi) {
        super(activity, R.style.Rent_Dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_title_message, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        Button btn_nega = (Button) view.findViewById(R.id.btn_nega);
        btn_nega.setText(negeText);
        View divider = view.findViewById(R.id.divider);
        Button btn_posi = (Button) view.findViewById(R.id.btn_posi);
        btn_posi.setText(posiText);
        tv_title.setText(title);
        if (TextUtils.isEmpty(message)) {
            tv_message.setVisibility(View.GONE);
        } else {
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText(message);
        }
        if (showNegaButton) {
            btn_nega.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            btn_nega.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (nega != null) {
                        nega.onClick(v);
                    }
                }
            });
        } else {
            btn_nega.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            btn_posi.setBackground(activity.getResources().getDrawable(R.drawable.dialog_singlebtn_selector));//一个按钮时,改成圆角按钮背景
        }

        btn_posi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (posi != null) {
                    posi.onClick(v);
                }
            }
        });
        setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ContextHouse.SCREENWIDTH * 0.76); //设置宽度屏幕的0.76
        getWindow().setAttributes(lp);
    }
}
