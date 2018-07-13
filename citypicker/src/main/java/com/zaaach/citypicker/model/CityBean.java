package com.zaaach.citypicker.model;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 城市bean
 */
public class CityBean {
    //    public String province;
    public String areaId;
    public String areaName;
    public String pinyin;
    public List<AreaBean> counties;//市内区,县

    public CityBean() {
    }

    public CityBean(String areaName, String pinyin, String areaId) {
        this.areaName = areaName;
//        this.province = province;
        this.pinyin = pinyin;
        this.areaId = areaId;
    }

    /***
     * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
     * @return
     */
    public String getSection() {
        if (TextUtils.isEmpty(pinyin)) {
            return "#";
        } else {
            String c = pinyin.substring(0, 1);
            Pattern p = Pattern.compile("[a-zA-Z]");
            Matcher m = p.matcher(c);
            if (m.matches()) {
                return c.toUpperCase();
            }
            //在添加定位和热门数据时设置的section就是‘定’、’热‘开头
            else if (TextUtils.equals(c, "当") || TextUtils.equals(c, "热") || TextUtils.equals(c, "定"))
                return pinyin;
            else
                return "#";
        }
    }
}
