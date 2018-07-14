package com.shen.house

import android.Manifest
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.ArrayMap
import android.util.SparseArray
import android.view.KeyEvent
import android.view.View
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.base.ContentViewWrap
import com.shen.baselibrary.utiles.ActivityStackManager
import com.shen.baselibrary.utiles.ToastUtile
import com.shen.baselibrary.utiles.resulttutils.PermissionCallBack
import com.shen.baselibrary.utiles.resulttutils.PermissionUtils
import com.shen.house.fragment1.HomeFragment
import com.shen.house.fragment2.SecondFragment
import com.shen.house.fragment3.ThirdFragment
import com.shen.house.fragment4.FourthFragment
import com.shen.house.post.PostActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    var mExitTime: Long = 0
    private var mFragment: Fragment? = null
    private val mFragments = SparseArray<Fragment>(4)
    override fun getcontentView(): Int {
        return R.layout.activity_main
    }

    override fun injectStatus(view: View): ContentViewWrap? {
        return null
    }

    override fun afterInjectView(view: View) {
        setEnableSwipeBack(-1)
        mFragments.put(R.id.rb1, HomeFragment())
        mFragments.put(R.id.rb2, SecondFragment())
        mFragments.put(R.id.rb3, ThirdFragment())
        mFragments.put(R.id.rb4, FourthFragment())
        initListener()
        rb1.setChecked(true)
        checkInternetPermission()
        checkUpdateApi()
        requestAdvert()//再其他之后调用
    }

    private fun checkInternetPermission() {
        PermissionUtils.requestPermission(`this`, Manifest.permission.INTERNET, object : PermissionCallBack() {
            override fun hasPermission() {

            }

            override fun refusePermission() {

            }

            override fun refusePermissionDonotAskAgain() {

            }
        })
    }

    private fun checkUpdateApi() {

    }

    /**
     * 请求广告位的信息 add_by_zyt
     */
    private fun requestAdvert() {
        val map = ArrayMap<String, Any>()
        map["type"] = "1"
//        HttpRequest.post(null, UrlConfig.GETADVERT, map, object : SimpleHttpCallback<AdvertBean>() {
//            @Throws(Exception::class)
//            protected fun onData(code: Int, data: AdvertBean, msg: String) {
//                isShowAd(data)
//            }
//
//            fun onError(act: StatusHandle, code: Int) {
//                LogUtils.i(TAG, code.toString() + " 首页弹框的信息加载失败")
//            }
//        })
    }

    private fun initListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var mTransaction = supportFragmentManager.beginTransaction()
            mFragment?.let { mTransaction.hide(mFragment) }
            val checkPos = group.checkedRadioButtonId
            val tag = checkPos.toString()
            mFragment = mFragments.get(checkPos)
            if (mFragment!!.tag != null) {
                mTransaction.show(mFragment)
            } else {
                val addFragment = supportFragmentManager.findFragmentByTag(tag)
                if (addFragment == null) {
                    mTransaction.add(R.id.flContainer, mFragment, tag)
                } else if (addFragment == mFragment) {
                    mTransaction.show(mFragment)
                } else {
                    mTransaction.remove(addFragment)
                    mTransaction.add(R.id.flContainer, mFragment, tag)
                }
            }
            mTransaction.commitNowAllowingStateLoss()
            //            mTransaction.commitAllowingStateLoss()
        }
        ivPost.setOnClickListener { v ->
            v.animate().withLayer().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction({ v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start() }).start()
            startActivity(Intent(`this`, PostActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK/*&&rbSales.isChecked()*/) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtile.showToast("再按一次退出程序")
                mExitTime = System.currentTimeMillis()
            } else {
//                StatisManager.getInstance().onExitApp("exit")
                ActivityStackManager.getInstance().popAllActivity()
//                StatisManager.getInstance().onPrepared(null)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
