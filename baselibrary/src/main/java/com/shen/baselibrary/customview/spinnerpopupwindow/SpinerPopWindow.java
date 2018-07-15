package com.shen.baselibrary.customview.spinnerpopupwindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.R;
import com.shen.baselibrary.base.BasePopWindow;
import com.shen.baselibrary.customview.recyclerview.DividerGridItemDecoration;
import com.shen.baselibrary.customview.recyclerview.RecycleViewDivider;
import com.shen.baselibrary.customview.recyclerview.WrapContentLinearLayoutManager;
import com.shen.baselibrary.helper.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;


public class SpinerPopWindow extends BasePopWindow implements BaseSpinerAdapter.ItemClickCallBack {

    private int mColunms = 1;
    private RecyclerView recyclerView;
    private View line;
    private Button btn_posi;
    private BaseSpinerAdapter mAdapter;
    private BaseSpinerAdapter.ItemClickCallBack onItemClickListener;
    private PosiButtonClickCallBack posiButtonClickListener;

    public SpinerPopWindow(Context context) {
        super(context);
    }

    @Override
    protected View getPopupView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_popup, null);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        line = view.findViewById(R.id.line);
        btn_posi = view.findViewById(R.id.btn_posi);
        return view;
    }


    public SpinerPopWindow setItemSelectListener(BaseSpinerAdapter.ItemClickCallBack listener) {
        onItemClickListener = listener;
        return this;
    }

    public SpinerPopWindow setPosiButtonClickListener(PosiButtonClickCallBack listener) {
        posiButtonClickListener = listener;
        return this;
    }

    public SpinerPopWindow setAdatper(BaseSpinerAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setOnItemClickListener(this);
        setColunms(mColunms);
        recyclerView.setAdapter(mAdapter);
        if (!mAdapter.singleCheck) {
            line.setVisibility(View.VISIBLE);
            btn_posi.setVisibility(View.VISIBLE);
            btn_posi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (posiButtonClickListener != null) {
                        List<BaseItem> allItems = mAdapter.getList();
                        List<BaseItem> selectItems = new ArrayList<>();
                        for (BaseItem item : allItems) {
                            if (item.isChecked) {
                                selectItems.add(item);
                            }
                        }
                        posiButtonClickListener.onClick(selectItems);
                        dismiss();
                    }
                }
            });
        }
        return this;
    }

    private void setViewWidthHeight(int colunms, BaseSpinerAdapter<? extends BaseItem> mAdapter) {
        RecyclerViewHolder viewHolder = mAdapter.onCreateViewHolder(recyclerView, 0);
        if (mAdapter.getItemCount() > 0) {
            mAdapter.onBindViewHolder(viewHolder, 0);//其实能测量下最宽的view更好，我这里就只第一个吧
        }
        View itemView = viewHolder.itemView;
        itemView.measure(0, 0);
        int itemHeight = itemView.getMeasuredHeight();
        int itemWidth = itemView.getMeasuredWidth();//
        int maxHight = ContextHouse.SCREENHEIGHT * 2 / 3;
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        int row = (int) Math.ceil(mAdapter.getItemCount() / colunms);
        if (itemHeight * row > maxHight) {
            layoutParams.height = maxHight;
        } else {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        layoutParams.width = itemWidth * colunms + colunms;//不知道为啥站不下，那就+个colunms个px吧
        mAdapter.setChildWidth(itemWidth);
    }

    @Override
    public void makeContentViewWidthExactly(int width) {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = width;
        mAdapter.setChildWidth(width / mColunms);

    }

    public SpinerPopWindow setColunms(int colunms) {
        if (mAdapter == null) {
            throw new RuntimeException("请先setAdapter");
        }
        mColunms = colunms;
        if (colunms == 1) {
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayout.HORIZONTAL, 1, mContext.getResources().getColor(R.color.line)));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, colunms));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, 1, mContext.getResources().getColor(R.color.line), false));
        }
        setViewWidthHeight(mColunms, mAdapter);//控制下recyclerView高度屏幕2/3,宽度
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

    public SpinerPopWindow setSelect(BaseItem item) {
        if (mAdapter != null) {
            mAdapter.setSelected(item);
        } else {
            throw new RuntimeException("先setAdapter()");
        }
        return this;
    }

    public SpinerPopWindow setSelect(@Nullable List<BaseItem> items) {
        if (mAdapter != null) {
            mAdapter.setSelected(items);
        } else {
            throw new RuntimeException("先setAdapter()");
        }
        return this;
    }

    @Override
    public void itemClick(int position, Object item) {
        if (onItemClickListener != null) {
            onItemClickListener.itemClick(position, item);
        }
        if (mAdapter.singleCheck) {
            recyclerView.postDelayed(new Runnable() {//选中后再消失,肉眼可见
                @Override
                public void run() {
                    dismiss();
                }
            }, 100);
        }
    }

    public interface PosiButtonClickCallBack<T> {
        /**
         * @param items 选中的条目
         */
        void onClick(@NonNull List<T> items);
    }
}
