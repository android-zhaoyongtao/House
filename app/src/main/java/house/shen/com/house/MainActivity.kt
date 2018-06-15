package house.shen.com.house

import android.view.View
import house.shen.com.house.base.BaseActivity
import house.shen.com.house.utiles.AnimateUtiles
import house.shen.com.house.utiles.ToastUtile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getcontentView(): Int {
        return R.layout.activity_main
    }

    override fun afterInjectView(view: View) {
        ivPost.setOnTouchListener { v, event ->
            return@setOnTouchListener AnimateUtiles.handleTouch(v, event, Runnable { ToastUtile.showToast("ivPost-click()") })
        }

        rb1.setChecked(true)
    }

}
