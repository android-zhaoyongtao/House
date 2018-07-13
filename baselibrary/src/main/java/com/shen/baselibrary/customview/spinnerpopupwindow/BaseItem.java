package com.shen.baselibrary.customview.spinnerpopupwindow;

import java.util.Objects;

public class BaseItem {
    public BaseItem() {
    }

    public BaseItem(String text) {
        this.text = text;
    }

    public String text = "";//下来要去掉的
    public boolean isChecked = false;

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
