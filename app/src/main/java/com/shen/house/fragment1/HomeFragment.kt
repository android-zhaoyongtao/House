package com.shen.house.fragment1

import android.view.View
import android.widget.TextView
import com.shen.baselibrary.base.BaseFragment
import com.shen.house.ConfigUtils
import com.shen.house.R

class HomeFragment : BaseFragment() {
    override fun getcontentView() = R.layout.fragment_home

    override fun afterInjectView(view: View) {
        view.findViewById<TextView>(R.id.tvCity).setText(ConfigUtils.getCurrentCity()?.areaName
                ?: "城市")
    }

    override fun getData() {
    }

}
