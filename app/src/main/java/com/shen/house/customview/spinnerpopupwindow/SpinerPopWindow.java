package com.shen.house.customview.spinnerpopupwindow;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shen.baselibrary.base.BasePopWindow;
import com.shen.baselibrary.customview.recyclerview.DividerGridItemDecoration;
import com.shen.baselibrary.customview.recyclerview.RecycleViewDivider;
import com.shen.baselibrary.customview.recyclerview.WrapContentLinearLayoutManager;
import com.shen.house.R;

import java.util.List;


public class SpinerPopWindow extends BasePopWindow implements BaseSpinerAdapter.ItemClickCallBack {


    private RecyclerView recyclerView;
    private BaseSpinerAdapter mAdapter;
    private BaseSpinerAdapter.ItemClickCallBack onItemClickListener;
    public SpinerPopWindow(Context context) {
        super(context);
    }

    @Override
    protected View getPopupView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_popup, null);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        setColunms(1);
        return view;
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
