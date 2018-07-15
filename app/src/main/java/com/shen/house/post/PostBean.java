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
    public String chaoxiang;
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

    private static class ShiTing {
        public Pair<Integer, String> shi;
        //        public String shis;
        public Pair<Integer, String> ting;
        //        public String tings;
        public Pair<Integer, String> wei;
//        public String weis;
    }

    private static class DianTi {
        public boolean has;
        public int ti;
        public int hu;
    }
}
