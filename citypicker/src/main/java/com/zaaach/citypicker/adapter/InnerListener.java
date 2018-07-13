package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.CityBean;

public interface InnerListener {
    void dismiss(int position, CityBean data);

    void locate();
}
