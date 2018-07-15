package com.shen.house.post;

import android.util.Pair;

import com.luck.picture.lib.entity.LocalMedia;
import com.shen.baselibrary.customview.spinnerpopupwindow.BaseItem;
import com.zaaach.citypicker.model.AreaBean;

import java.util.List;

public class PostBean {
    public String title;
    public String content;
    public List<LocalMedia> pics;
    public String name;
    public AreaBean area;//区县
    public String address;
    public float mianji;
    public ShiTing shiting;
    public BaseItem chaoxiang;
    public DianTi dianti;
    public float shoufu;
    public float daikuan;
    public float qitafeiyong;
    public int fangling;
    public float zongjia;
    public float danjia;
    public Pair<Integer, Integer> louceng;
    public Pair<Integer, String> zhuangxiu;
    public Pair<Integer, String> xingzhi;
    public List<BaseItem> wuzhengs;//选中的

    public static class ShiTing {
        public int shi;
        public int ting;
        public int wei;

        public ShiTing(int shi, int ting, int wei) {
            this.shi = shi;
            this.ting = ting;
            this.wei = wei;
        }
    }

    public static class DianTi {
        public boolean has;
        public int ti;
        public int hu;

        public DianTi(boolean has, int ti, int hu) {
            this.has = has;
            this.ti = ti;
            this.hu = hu;
        }
    }
}
