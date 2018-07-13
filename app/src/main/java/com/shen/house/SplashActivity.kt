package com.shen.house

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.utiles.FileUtils
import com.shen.baselibrary.utiles.resulttutils.PermissionCallBack
import com.shen.baselibrary.utiles.resulttutils.PermissionUtils
import java.io.File

class SplashActivity : BaseActivity() {
    override fun getcontentView() = -1

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
        if (ConfigUtils.getCurrentCity()!=null) {
            startActivity(Intent(`this`, MainActivity::class.java))
        }else{
            startActivities(arrayOf(Intent(`this`, MainActivity::class.java),Intent(`this`, CityActivity::class.java)))
        }

        overridePendingTransition(0, 0);
        finish()
    }
}
