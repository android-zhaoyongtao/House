package com.shen.house

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.utiles.ExecutorUtile
import com.shen.baselibrary.utiles.FileUtils
import com.shen.baselibrary.utiles.resulttutils.PermissionCallBack
import com.shen.baselibrary.utiles.resulttutils.PermissionUtils
import com.zaaach.citypicker.LocationUtils
import com.zaaach.citypicker.model.CityBean
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
                ExecutorUtile.runInSubThred { File(FileUtils.getCacheDir(`this`), ".nomedia").mkdirs() }
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
        if (LocationUtils.getSPCity() != null) {
            startActivity(Intent(`this`, MainActivity::class.java))
            overridePendingTransition(0, 0);
            finish()
        } else {
            LocationUtils.toSelectPage(`this`, object : LocationUtils.CityCallBack {
                override fun call(city: CityBean?) {
                    startActivity(Intent(`this`, MainActivity::class.java))
                    overridePendingTransition(0, 0);
                    finish()
                }
            })
        }
    }
}
