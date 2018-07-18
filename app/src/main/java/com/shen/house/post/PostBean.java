package com.shen.house.post;

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
    public String mianji;//float
    public ShiTing shiting;
    public BaseItem chaoxiang;
    public DianTi dianti;
    public String fangling;//int
    public String danjia;//float
    public String zongjia;//float
    public String shoufu;//float
    public String daikuan;//float
    public String qita;//float
    public String louceng;//int
    public String alllouceng;//int
    public BaseItem zhuangxiu;
    public BaseItem xingzhi;
    public List<BaseItem> wuzhengs;//选中的
    public String phone;//int
    public String weixin;
    public String qq;//int

    public boolean isNotEmpty() {
        return false;
    }

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
