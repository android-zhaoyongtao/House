package com.shen.baselibrary.utiles.resulttutils

import android.content.Intent

interface ResultCallback {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}