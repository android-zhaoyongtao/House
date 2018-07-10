package com.shen.house.customview.spinnerpopupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.shen.house.R;

import java.util.List;


public class AbstractSpinerAdapter<T extends BaseItem> extends BaseAdapter {

    public interface IOnItemSelectListener {
        void onItemClick(int pos);
    }


    private Context mContext;
    private List<T> mObjects;
    public boolean singleCheck = false;//是否单选


    public AbstractSpinerAdapter(Context context) {
        init(context);
    }

    public void refreshData(List<T> objects, int selIndex) {
        mObjects = objects;
        if (mObjects != null && selIndex < objects.size() - 1 && selIndex > 0) {
            if (singleCheck) {
                for (T object : mObjects) {
                    object.isChecked = false;
                }
            }
            mObjects.get(selIndex).isChecked = true;
        }
        notifyDataSetChanged();
    }

    private void init(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {

        return mObjects == null ? 0 : mObjects.size();
    }

    @Override
    public BaseItem getItem(int pos) {
        return mObjects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spiner_muliple, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        BaseItem item = getItem(pos);
        viewHolder.mTextView.setText(item.text);
        viewHolder.mTextView.setChecked(item.isChecked);
        return convertView;
    }

    public static class ViewHolder {
        public CheckedTextView mTextView;
    }


}
