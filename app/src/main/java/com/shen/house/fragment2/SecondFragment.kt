package com.shen.house.fragment2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.DecodeFormat
import com.shen.baselibrary.base.BaseFragment
import com.shen.house.R
import com.shen.house.glide.ImageLoader

class SecondFragment : BaseFragment() {
    override fun getcontentView() = R.layout.fragment_second

    override fun afterInjectView(view: View) {
        var url = "http://image2.sina.com.cn/dy/o/2004-11-10/1100077821_2laygS.jpg"
        var imageView = view.findViewById<ImageView>(R.id.imageView)
        var tvClear = view.findViewById<TextView>(R.id.tvClear)
        ImageLoader.getInstance().displayImage(`this`, imageView, url, DecodeFormat.PREFER_ARGB_8888)
        tvClear.setOnClickListener { ImageLoader.getInstance().clearCache(`this`) }
    }
}