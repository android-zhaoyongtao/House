package com.shen.house.customview.spinnerpopupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.shen.baselibrary.customview.recyclerview.DividerGridItemDecoration;
import com.shen.baselibrary.customview.recyclerview.RecycleViewDivider;
import com.shen.baselibrary.customview.recyclerview.WrapContentLinearLayoutManager;
import com.shen.house.R;

import java.util.List;


public class SpinerPopWindow extends PopupWindow implements BaseSpinerAdapter.ItemClickCallBack {

    private Context mContext;
    private RecyclerView recyclerView;
    private BaseSpinerAdapter mAdapter;
    private BaseSpinerAdapter.ItemClickCallBack onItemClickListener;


    public SpinerPopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public SpinerPopWindow setItemSelectListener(BaseSpinerAdapter.ItemClickCallBack listener) {
        onItemClickListener = listener;
        return this;
    }

    public SpinerPopWindow setAdatper(BaseSpinerAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        return this;
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_popup, null);
        setContentView(view);


        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        setColunms(1);
    }

    public SpinerPopWindow setColunms(int colunms) {
        if (colunms == 1) {
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayout.HORIZONTAL, 1, mContext.getResources().getColor(R.color.line)));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, colunms));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, 1, mContext.getResources().getColor(R.color.line), false));
        }
        return this;
    }

    public <T> SpinerPopWindow setData(List<T> list) {
        if (mAdapter != null) {
            mAdapter.set(list);
        } else {
            throw new RuntimeException("先setAdapter()");
        }
        return this;
    }

    public SpinerPopWindow setSelect(int selctIndex) {
        if (mAdapter != null) {
            mAdapter.setSelected(selctIndex);
        } else {
            throw new RuntimeException("先setAdapter()");
        }
        return this;
    }


    public SpinerPopWindow showPopupWindow(View parent) {
        if (!this.isShowing()) {
            setWidth(parent.getWidth());
            setHeight(LayoutParams.WRAP_CONTENT);
            this.showAsDropDown(parent);
            backgroundAlpha((Activity) mContext, 0.5f);
        } else {
            this.dismiss();
        }
        return this;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(Activity context, float bgAlpha) {
        if (context instanceof Activity) {
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            context.getWindow().setAttributes(lp);
        }
    }


    @Override
    public <T extends BaseItem> void itemClick(int position, T item) {
        if (onItemClickListener != null) {
            onItemClickListener.itemClick(position, item);
            if (mAdapter.singleCheck) {
                recyclerView.postDelayed(new Runnable() {//选中后再消失,肉眼可见
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 100);
            }
        }
    }
}
