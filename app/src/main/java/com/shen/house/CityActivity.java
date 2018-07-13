package com.shen.house;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.shen.baselibrary.base.BaseActivity;
import com.shen.baselibrary.utiles.ToastUtile;
import com.shen.house.utiles.LocationUtils;
import com.zaaach.citypicker.CityPicker;
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
                        .setLocatedCity(null)
//                        .setHotCities(hotCities)
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, CityBean data) {
                                ToastUtile.showToast(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
//                                finish();
//                                ((TextView) v).setText(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
                            }

                            @Override
                            public void onLocate() {
                                //开始定位，这里模拟一下定位
                                getLocation();
                            }
                        })
                        .show();


                //定位请求
//                getLocation("假装成功", LocateState.SUCCESS, 0);
                getLocation();
            }
        });

    }

    private void getLocation() {
        LocationUtils.getCity(getThis(), new LocationUtils.CityCallBack() {
            @Override
            public void call(@Nullable CityBean city) {
                if (city != null) {
                    tv.setText(city.areaName);
                    CityPicker.getInstance().locateComplete(city, LocateState.SUCCESS);
                }else {
                    CityPicker.getInstance().locateComplete(city, LocateState.FAILURE);
                }

            }
        });
    }

}
