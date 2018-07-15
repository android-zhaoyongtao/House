package com.shen.baselibrary.customview.spinnerpopupwindow;

import java.util.Objects;

public class BaseItem {
    public BaseItem() {
    }

    public BaseItem(String text) {
        this.text = text;
    }

    private String text = "";//搞成private否则子类不好覆盖
    public boolean isChecked = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "text='" + text + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseItem)) return false;
        BaseItem baseItem = (BaseItem) o;
        return Objects.equals(text, baseItem.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
