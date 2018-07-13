package com.zaaach.citypicker.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shen.baselibrary.http.Convert;
import com.zaaach.citypicker.HanziToPinyin;
import com.zaaach.citypicker.model.AreaBean;
import com.zaaach.citypicker.model.CityBean;
import com.zaaach.citypicker.model.ProvinceBean;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitysManager {

    private Context mContext;

    public CitysManager(Context context) {
        this.mContext = context;
    }

    /**
     * @return 排好序的城市
     */
    public @NonNull
    List<CityBean> getAllCities() {
        try {
            InputStream is = mContext.getAssets().open("city.json");
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            JsonReader jsonReader = new JsonReader(reader);
            ArrayList<ProvinceBean> provinceBeans = Convert.fromJson(jsonReader, new TypeToken<ArrayList<ProvinceBean>>() {
            }.getType());
            reader.close();
            is.close();
            ArrayList<CityBean> cities = new ArrayList<>(360);
            for (ProvinceBean province : provinceBeans) {
                for (CityBean cityBean : province.cities) {
                    cityBean.pinyin = (HanziToPinyin.getPinYin(cityBean.areaName));//设置拼音
                    cities.add(cityBean);
                }
            }
            Collections.sort(cities, new CityComparator());
            return cities;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<CityBean> searchCity(List<CityBean> allCities, final String keyword) {
        if (allCities == null || allCities.isEmpty()) {
            allCities = getAllCities();
        }
        List<CityBean> resultCities = new ArrayList<>();
        for (CityBean cityBean : allCities) {
            if (cityBean.areaName.contains(keyword) || cityBean.pinyin.contains(keyword)) {
                resultCities.add(cityBean);
            }
        }
        return resultCities;
    }

    public @Nullable
    List<AreaBean> allAreaInCity(String cityId) {
        if (TextUtils.isEmpty(cityId)) {
            return null;
        }
        for (CityBean cityBean : getAllCities()) {
            if (cityId.equals(cityBean.areaId)) {
                return cityBean.counties;
            }
        }
        return null;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<CityBean> {
        @Override
        public int compare(CityBean lhs, CityBean rhs) {
            String a = lhs.pinyin.substring(0, 1);
            String b = rhs.pinyin.substring(0, 1);
            return a.compareTo(b);
        }
    }
}
