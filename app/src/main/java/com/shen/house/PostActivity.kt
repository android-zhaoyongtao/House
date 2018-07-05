package com.shen.house

import android.view.View
import com.shen.baselibrary.base.BaseActivity
import kotlinx.android.synthetic.main.include_title.*

//发布页面
class PostActivity : BaseActivity() {
    override fun getcontentView(): Int {
        return R.layout.activity_post
    }


    override fun afterInjectView(view: View) {
        titleBack.setOnClickListener { finish() }
        titleText.setText("发布信息")
    }

}
