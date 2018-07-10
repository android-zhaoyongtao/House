package com.shen.house.customview.spinnerpopupwindow;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shen.baselibrary.helper.EViewHolder;
import com.shen.baselibrary.helper.RecyclerAdapter;
import com.shen.baselibrary.utiles.StringUtils;
import com.shen.house.R;

import java.util.List;


public class BaseSpinerAdapter<T extends BaseItem> extends RecyclerAdapter<T> {
    public boolean singleCheck = false;//是否单选
    private ItemClickCallBack callBack;

    public void setOnItemClickListener(ItemClickCallBack callBack) {
        this.callBack = callBack;
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
    public void onSetView(final EViewHolder holder, final T item, final int position) {
        TextView textView = holder.getViewById(R.id.textView);
        CompoundButton checkBox = holder.getViewById(R.id.checkBox);
        CompoundButton radioButton = holder.getViewById(R.id.radioButton);
        textView.setText(item.text);
        textView.setTextColor(mContext.getResources().getColor(item.isChecked ? R.color.colorAccent : R.color.grey));
        if (singleCheck) {
            radioButton.setChecked(item.isChecked);
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setChecked(item.isChecked);
            radioButton.setVisibility(View.GONE);
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(position);
                if (callBack != null) {
                    callBack.itemClick(position, item);
                }
            }
        });
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
                    mList.get(selectIndex).isChecked = !mList.get(selectIndex).isChecked;
                    notifyDataSetChanged();
                } else {
                    mList.get(selectIndex).isChecked = !mList.get(selectIndex).isChecked;
                    notifyItemChanged(selectIndex);
                }
            }
        }
    }

    public interface ItemClickCallBack {
        <T extends BaseItem> void itemClick(int position, T item);
    }
}
