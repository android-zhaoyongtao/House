package com.zaaach.citypicker.model;

import com.google.gson.annotations.SerializedName;
import com.shen.baselibrary.customview.spinnerpopupwindow.BaseItem;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaBean)) return false;
        if (!super.equals(o)) return false;
        AreaBean areaBean = (AreaBean) o;
        return Objects.equals(areaId, areaBean.areaId) &&
                Objects.equals(text, areaBean.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaId, text);
    }
}