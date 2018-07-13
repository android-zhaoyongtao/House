package com.shen.house.utiles;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shen.baselibrary.utiles.ExecutorUtile;
import com.shen.baselibrary.utiles.resulttutils.PermissionCallBack;
import com.shen.baselibrary.utiles.resulttutils.PermissionUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.CityBean;

import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static void getLocation(final Activity context, final LocationCallBack callBack) {
        final LocationListener[] locationListener = new LocationListener[1];
        PermissionUtils.requestPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallBack() {
            @Override
            public void refusePermission() {
            }

            @Override
            public void refusePermissionDonotAskAgain() {
                PermissionUtils.toAppSetting(context);
            }

            @SuppressLint("MissingPermission")
            @Override
            public void hasPermission() {
                final LocationManager myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                locationListener[0] = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        myLocationManager.removeUpdates(this);
                        locationListener[0] = null;
                        if (location != null) {
                            //获取国家，省份，城市的名称
//                            String str = "getLatitude:" + location.getLatitude() + ";getLongitude:" + location.getLongitude() + ";getTime:" + location.getTime() + ";getProvider:" + location.getProvider();

//                            Log.e("location24", "changed:" + str);
//                List<Address> m_list = getAddress(location);
//                ExecutorUtile.runInSubThred();
//                new MyAsyncExtue().execute(location);
//                Log.e("str", m_list.toString());
//                String city = "";
////                if (m_list != null && m_list.size() > 0) {
////                    city = m_list.get(0).getLocality();//获取城市
////                }
//                city = m_list;
                        } else {

                        }
                        if (callBack != null) {
                            callBack.call(location);
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                };
                //2000代表每2000毫秒更新一次，5代表每5秒更新一次
                myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 1, locationListener[0]);

                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, locationListener[0]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (locationListener[0] != null) {
                            Location netLocation = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            Location gpsLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Location location;
                            if (gpsLocation == null && netLocation == null) {
                                location = null;
                            } else if (gpsLocation != null && netLocation != null) {
                                if (gpsLocation.getTime() < netLocation.getTime()) {
                                    location = netLocation;
                                } else {
                                    location = gpsLocation;
                                }
                            } else if (gpsLocation == null) {
                                location = netLocation;
                            } else {
                                location = gpsLocation;
                            }
                            locationListener[0].onLocationChanged(location);
                        }
                    }
                }, 4000);
            }
        });
    }

    public static void getCity(final Activity activity, @NonNull final CityCallBack cityCallBack) {
        //1自带
        //2,百度返回百度码
        //3本地https://blog.csdn.net/qq_24636637/article/details/50461284
        getLocation(activity, new LocationCallBack() {
            @Override
            public void call(@Nullable final Location location) {
                if (location == null) {
                    cityCallBack.call(null);
                } else {
                    ExecutorUtile.runInSubThred(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Geocoder gc = new Geocoder(activity, Locale.getDefault());
                                List<Address> result = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                result = gc.getFromLocation(38.75, 114.98, 1);
//                                    LogUtils.e("location25", "address:" + result.get(0).toString());
//                                LogUtils.e("location25", "address:" + result.get(0).getLocality());
                                String cityName = result.get(0).getLocality();
                                String shortCity = cityName.replaceAll("市", "");
                                List<CityBean> allCities = new DBManager(activity).getAllCities();
                                for (CityBean city : allCities) {
                                    if (city.areaName.contains(shortCity)) {
                                        cityCallBack.call(city);
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                cityCallBack.call(null);
                            }

                        }
                    });
                }
            }
        });
    }

    public interface LocationCallBack {
        void call(@Nullable Location location);
    }

    public interface CityCallBack {
        void call(@Nullable CityBean city);
    }
}
