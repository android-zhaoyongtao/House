package com.shen.house.customview.spinnerpopupwindow;

public class BaseItem {
    public BaseItem() {
    }

    public BaseItem(String text) {
        this.text = text;
    }
    public String text = "";
    public boolean isChecked = false;

    @Override
    public String toString() {
        return "BaseItem{" +
                "text='" + text + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
