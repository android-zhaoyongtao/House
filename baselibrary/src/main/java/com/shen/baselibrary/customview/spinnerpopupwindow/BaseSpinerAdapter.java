package com.shen.baselibrary.customview.spinnerpopupwindow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shen.baselibrary.R;
import com.shen.baselibrary.helper.EViewHolder;
import com.shen.baselibrary.helper.RecyclerAdapter;
import com.shen.baselibrary.utiles.StringUtils;

import java.util.List;


public class BaseSpinerAdapter<T extends BaseItem> extends RecyclerAdapter<T> implements AdapterView.OnItemClickListener {
    public boolean singleCheck = false;//是否单选
    private ItemClickCallBack callBack;
    private int chileNewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

    public void setOnItemClickListener(ItemClickCallBack callBack) {
        this.callBack = callBack;
        setOnItemClickListener(this);
    }

    public BaseSpinerAdapter(Context context) {
        super(context, null);
    }

    public BaseSpinerAdapter(Context context, boolean singleCheck) {
        super(context, null);
        this.singleCheck = singleCheck;
    }

    public BaseSpinerAdapter(Context context, List<T> list, boolean singleCheck) {
        super(context, list);
        this.singleCheck = singleCheck;
    }

    @Override
    protected int onCreateViewLayoutID(int viewType) {
        return R.layout.item_spiner_popup;
    }

    @Override
    public void onSetView(final EViewHolder holder, final BaseItem item, final int position) {
        holder.getConvertView().getLayoutParams().width = chileNewWidth;
        TextView textView = holder.getViewById(R.id.textView);
        CompoundButton checkBox = holder.getViewById(R.id.checkBox);
        CompoundButton radioButton = holder.getViewById(R.id.radioButton);
        String text = item.getText();
        textView.setText(text);
        textView.setTextColor(mContext.getResources().getColor(item.isChecked ? R.color.blue : R.color.grey));
        if (singleCheck) {
            radioButton.setChecked(item.isChecked);
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setChecked(item.isChecked);
            radioButton.setVisibility(View.GONE);
        }
    }


    private void changeSelected(int selectIndex) {
        if (singleCheck) {
            for (T object : mList) {
                object.isChecked = false;
            }
            mList.get(selectIndex).isChecked = !mList.get(selectIndex).isChecked;
            notifyDataSetChanged();
        } else {
            mList.get(selectIndex).isChecked = !mList.get(selectIndex).isChecked;
            notifyItemChanged(selectIndex);
        }
    }

    public void setSelected(int selectIndex) {
        if (StringUtils.listSize(mList) <= 0) {
            throw new RuntimeException("先设置非空mList数据");
        } else {
            if (selectIndex < mList.size() && selectIndex >= 0) {
                if (singleCheck) {
                    for (T object : mList) {
                        object.isChecked = false;
                    }
                    mList.get(selectIndex).isChecked = true;
                    notifyDataSetChanged();
                } else {
                    mList.get(selectIndex).isChecked = true;
                    notifyItemChanged(selectIndex);
                }
            }
        }
    }

    public void setSelected(BaseItem item) {
        if (StringUtils.listSize(mList) <= 0) {
            throw new RuntimeException("先设置非空mList数据");
        } else {
            int index = mList.indexOf(item);
            if (index >= 0) {
                if (singleCheck) {
                    for (T object : mList) {
                        object.isChecked = false;
                    }
                    mList.get(index).isChecked = true;
                    notifyDataSetChanged();
                } else {
                    mList.get(index).isChecked = true;
                    notifyItemChanged(index);
                }
            }
        }
    }

    public void setSelected(@Nullable List<BaseItem> items) {
        if (StringUtils.listSize(mList) <= 0) {
            throw new RuntimeException("先设置非空mList数据");
        } else if (items == null || items.isEmpty()) {
        } else {
            if (singleCheck) {
                int index = 0;
                if (!items.isEmpty() && (index = mList.indexOf(items.get(0))) >= 0) {
                    for (T object : mList) {
                        object.isChecked = false;
                    }
                    mList.get(index).isChecked = true;
                    notifyDataSetChanged();
                }
            } else {
                for (BaseItem item : items) {
                    int index = 0;
                    if ((index = mList.indexOf(item)) >= 0) {
                        mList.get(index).isChecked = true;
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        changeSelected(position);
        if (callBack != null) {
            callBack.itemClick(position, mList.get(position));
        }
    }

    public void setChildWidth(int width) {
        this.chileNewWidth = width;
    }

    public interface ItemClickCallBack<T> {
        void itemClick(int position, T item);
    }
}
