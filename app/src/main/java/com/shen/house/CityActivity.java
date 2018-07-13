package com.shen.house;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.shen.baselibrary.base.BaseActivity;
import com.shen.baselibrary.utiles.ToastUtile;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.CityBean;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCityBean;

public class CityActivity extends BaseActivity {


    @Override
    public int getcontentView() {
        setTheme(R.style.DefaultCityPickerTheme);
        return R.layout.activity_city;
    }

    @Override
    public void afterInjectView(@NonNull View view) {
        view.findViewById(R.id.tvLoadingDesc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                CityPicker.getInstance()
                        .setFragmentManager(getSupportFragmentManager())
                        .enableAnimation(false)
                        .setAnimationStyle(R.style.DefaultCityPickerAnimation)
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
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        CityPicker.getInstance().locateComplete(new LocatedCityBean("失败了", ""), LocateState.FAILURE);
                                    }
                                }, 3000);
                            }
                        })
                        .show();
                //定位请求
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CityPicker.getInstance().locateComplete(new LocatedCityBean("假装成功", ""), LocateState.SUCCESS);
                    }
                }, 0);
            }
        });

    }
}
