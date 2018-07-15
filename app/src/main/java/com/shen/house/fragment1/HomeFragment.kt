package com.shen.house.fragment1

import android.view.View
import android.widget.TextView
import com.shen.baselibrary.base.BaseFragment
import com.shen.house.R
import com.zaaach.citypicker.LocationUtils
import com.zaaach.citypicker.model.CityBean

class HomeFragment : BaseFragment() {
    override fun getcontentView() = R.layout.fragment_home

    override fun afterInjectView(view: View) {
        val tvCity = view.findViewById<TextView>(R.id.tvCity)
        tvCity.setText(LocationUtils.getSPCity()?.areaName ?: "城市")
        tvCity.setOnClickListener {
            LocationUtils.toSelectPage(activity, object : LocationUtils.CityCallBack {
                override fun call(city: CityBean?) {
                    city?.let { tvCity.text = city.areaName }
                }
            })
        }
    }

    override fun getData() {
    }

}
