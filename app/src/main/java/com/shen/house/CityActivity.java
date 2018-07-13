package com.shen.house;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.shen.baselibrary.base.BaseActivity;
import com.shen.house.utiles.LocationUtils;
import com.zaaach.citypicker.model.CityBean;

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

//                CityPicker.getInstance()
//                        .setFragmentManager(getSupportFragmentManager())
//                        .enableAnimation(true)
////                        .setAnimationStyle(R.style.DefaultCityPickerAnimation)
//                        .setLocatedCity(null)
////                        .setHotCities(hotCities)
//                        .setOnPickListener(new OnPickListener() {
//                            @Override
//                            public void onPick(int position, CityBean data) {
//                                ToastUtile.showToast(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
////                                finish();
////                                ((TextView) v).setText(data == null ? "点的空" : String.format("点击的数据：%s，%s", data.areaName, data.areaId));
//                            }
//
//                            @Override
//                            public void onLocate() {
//                                //开始定位，这里模拟一下定位
//                                getLocation("失败了", LocateState.FAILURE, 3000);
//                            }
//                        })
//                        .show();


                //定位请求
//                getLocation("假装成功", LocateState.SUCCESS, 0);
                LocationUtils.getCity(getThis(), new LocationUtils.CityCallBack() {
                    @Override
                    public void call(@Nullable CityBean city) {
                        if (city != null) {
                            tv.setText(city.areaName);
                        }
                    }
                });
//                LocationUtils.getLocation(getThis(), new LocationUtils.LocationCallBack() {
//                    @Override
//                    public void call(final Location location) {
//                        if (location==null) {
//                            return;
//                        }
//                        String str = "getLatitude:" + location.getLatitude() + ";getLongitude:" + location.getLongitude() + ";getTime:" + location.getTime() + ";getProvider:" + location.getProvider();
//                        LogUtils.e("location23", "经纬度:" + str);
//                        ExecutorUtile.runInSubThred(new Runnable() {
//                            @Override
//                            public void run() {
//                                List<Address> result = null;
//                                try {
//                                    if (location != null) {
//                                        Geocoder gc = new Geocoder(getThis(), Locale.getDefault());
//                                        result = gc.getFromLocation(location.getLatitude(),  location.getLongitude(), 1);
////                                        result = gc.getFromLocation(38.75,  114.98, 1);
//                                    }
////                                    LogUtils.e("location25", "address:" + result.get(0).toString());
//                                    LogUtils.e("location25", "address:" + result.get(0).getLocality());
//                                    //1自带
//                                    //2,百度返回百度码
//                                    //3本地https://blog.csdn.net/qq_24636637/article/details/50461284
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//
//                    }
//                });
            }
        });

    }

}
