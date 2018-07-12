package com.shen.house

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.base.ContentViewWrap
import com.shen.baselibrary.utiles.ActivityStackManager
import com.shen.baselibrary.utiles.ToastUtile
import com.shen.house.post.PostActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    var mExitTime: Long = 0
    override fun getcontentView(): Int {
        return R.layout.activity_main
    }

    override fun injectStatus(view: View): ContentViewWrap? {
        return null
    }

    override fun afterInjectView(view: View) {
        setEnableSwipeBack(-1)
        ivPost.setOnClickListener { v ->
            v.animate().withLayer().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction({ v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start() }).start()
            startActivity(Intent(`this`, PostActivity::class.java))
        }

        rb1.setChecked(true)
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
