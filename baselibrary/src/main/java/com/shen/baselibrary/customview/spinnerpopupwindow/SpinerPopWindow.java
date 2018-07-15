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

import java.util.ArrayList;
import java.util.List;


public class SpinerPopWindow extends BasePopWindow implements BaseSpinerAdapter.ItemClickCallBack {


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
        setColunms(1);
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
        //控制下recyclerView高度屏幕2/3
        View itemView = mAdapter.onCreateViewHolder(recyclerView, 0).itemView;
        itemView.measure(0, 0);
        int relheight = itemView.getMeasuredHeight();
        int relwidth = itemView.getMeasuredWidth();
        int scrheight = ContextHouse.SCREENHEIGHT * 2 / 3;
        if (relheight * mAdapter.getItemCount() > scrheight) {
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.width = relwidth;
//            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = scrheight;
        }
        return this;
    }
    @Override
    public void makeContentViewWidthExactly(int width) {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = width;
        mAdapter.setChileMatchParent(true);

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
