package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.CityBean;

public interface OnPickListener {
    void onPick(int position, CityBean data);

    void onLocate();
}
