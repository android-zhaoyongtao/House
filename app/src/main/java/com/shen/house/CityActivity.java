package com.shen.house;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.shen.baselibrary.base.BaseActivity;
import com.shen.baselibrary.utiles.SPUtils;
import com.shen.baselibrary.utiles.ToastUtile;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.LocationUtils;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.CityBean;
import com.zaaach.citypicker.model.LocateState;

public class CityActivity extends BaseActivity {


    @Override
    public int getcontentView() {
        setTheme(R.style.DefaultCityPickerTheme);
        return R.layout.activity_city;
    }

    TextView tv;

    @Override
    public void afterInjectView(@NonNull View view) {
        tv = view.findViewById(R.id.tvLoadingDesc);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CityPicker.getInstance()
                        .setFragmentManager(getSupportFragmentManager())
                        .enableAnimation(true)
//                        .setAnimationStyle(R.style.DefaultCityPickerAnimation)
                        .setLocatedCity(SPUtils.getJsonObject(Key.SPKEY.SPKEY_CITYINFO, CityBean.class))
//                        .setHotCities(hotCities)
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, CityBean data) {
                                if (data != null) {
                                    data.pinyin = "当前城市";
                                    SPUtils.setJsonObject(Key.SPKEY.SPKEY_CITYINFO, data);
                                }
                                ToastUtile.showToast(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
                                ((TextView) v).setText(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
                            }

                            @Override
                            public void onLocate() {
                                //开始定位，这里模拟一下定位
                                getLocation();
                            }
                        })
                        .show();
            }
        });

    }

    private void getLocation() {
        LocationUtils.locationCity(getThis(), new LocationUtils.CityCallBack() {
            @Override
            public void call(@Nullable CityBean city) {
                if (city != null) {
                    tv.setText(city.areaName);
                    ToastUtile.showToast("定位成功");
                    CityPicker.getInstance().locateComplete(city, LocateState.SUCCESS);
                    SPUtils.setJsonObject(Key.SPKEY.SPKEY_CITYINFO, city);
                } else {
                    CityPicker.getInstance().locateComplete(new CityBean(getString(com.zaaach.citypicker.R.string.cp_locate_failed), "当前城市", "0"), LocateState.FAILURE);
                }

            }
        });
    }

}
