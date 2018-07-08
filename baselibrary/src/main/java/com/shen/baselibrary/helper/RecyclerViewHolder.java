package com.shen.baselibrary.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    EViewHolder viewHolder;

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        viewHolder = EViewHolder.getViewHolder(itemView);
        viewHolder.setViewHolder(this);
    }

    public EViewHolder getViewHolder() {
        return viewHolder;
    }
}
