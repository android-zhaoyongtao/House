package com.shen.house

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.utiles.FileUtils
import com.shen.baselibrary.utiles.ToastUtile
import com.shen.baselibrary.utiles.resulttutils.PermissionCallBack
import com.shen.baselibrary.utiles.resulttutils.PermissionUtils
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.LocationUtils
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.CityBean
import com.zaaach.citypicker.model.LocateState
import java.io.File

class SplashActivity : BaseActivity() {
    override fun getcontentView() = R.layout.activity_splash

    override fun injectStatus(view: View) = null

    override fun afterInjectView(view: View) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.requestPermission(`this`, Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallBack() {
            override fun hasPermission() {
                toNextActivity()
                File(FileUtils.getCacheDir(`this`), ".nomedia").mkdirs()
            }

            override fun refusePermission() {
                toNextActivity()
            }

            override fun refusePermissionDonotAskAgain() {
                toNextActivity()
            }
        })

    }

    private fun toNextActivity() {
//        if (ConfigUtils.getCurrentCity() != null) {
        if (true) {
            startActivity(Intent(`this`, MainActivity::class.java))
            overridePendingTransition(0, 0);
            finish()
        } else {
//            startActivities(arrayOf(Intent(`this`, MainActivity::class.java),Intent(`this`, CityActivity::class.java)))
            CityPicker.getInstance()
                    .setFragmentManager(supportFragmentManager)
                    .enableAnimation(true)
                    .setLocatedCity(ConfigUtils.getCurrentCity())
                    .setOnPickListener(object : OnPickListener {
                        override fun onPick(position: Int, data: CityBean?) {
                            if (data != null) {
                                data.pinyin = "当前城市"
                                ConfigUtils.setCurrentCity(data)
                            } else {
                                ConfigUtils.setCurrentCity(CityBean("北京", "当前城市", ""))
                            }
                            startActivity(Intent(`this`, MainActivity::class.java))
                            overridePendingTransition(0, 0);
                            finish()
                        }

                        override fun onLocate() {
                            //开始定位，这里模拟一下定位
                            getLocation()
                        }
                    })
                    .show()
        }


    }

    private fun getLocation() {
        LocationUtils.locationCity(`this`) { city ->
            if (city != null) {
                ToastUtile.showToast("定位成功")
                CityPicker.getInstance().locateComplete(city, LocateState.SUCCESS)
                ConfigUtils.setCurrentCity(city)
            } else {
                CityPicker.getInstance().locateComplete(CityBean(getString(com.zaaach.citypicker.R.string.cp_locate_failed), "当前城市", "0"), LocateState.FAILURE)
            }
        }
    }
}
