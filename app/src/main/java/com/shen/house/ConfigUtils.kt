package com.shen.house

import com.shen.baselibrary.utiles.SPUtils
import com.zaaach.citypicker.model.CityBean

object ConfigUtils {
    fun getCurrentCity(): CityBean? = SPUtils.getJsonObject(Key.SPKEY.SPKEY_CITYINFO, CityBean::class.java)
}