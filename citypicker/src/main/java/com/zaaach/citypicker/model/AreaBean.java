package com.zaaach.citypicker.model;

import com.google.gson.annotations.SerializedName;
import com.shen.baselibrary.customview.spinnerpopupwindow.BaseItem;

/**
 * 区bean
 */
public class AreaBean extends BaseItem {
    public String areaId;//区id
//    public String areaName;//区name
    @SerializedName("areaName")
    private String text;//区name

    public String getText() {
        return text;
    }
}