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
                File(FileUtils.getCacheDir(`this`), ".nomedia").mkdirs()
            }
        })
        startActivity(Intent(`this`, MainActivity::class.java))
        finish()
    }
}
