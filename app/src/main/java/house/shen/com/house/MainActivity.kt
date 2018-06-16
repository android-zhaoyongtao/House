package house.shen.com.house

import android.view.View
import android.widget.Toast
import house.shen.com.house.base.BaseActivity
import house.shen.com.house.base.ContentViewWrap
import house.shen.com.house.utiles.AnimateUtiles
import house.shen.com.house.utiles.ToastUtile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getcontentView(): Int {
        return R.layout.activity_main
    }

    override fun injectStatus(view: View): ContentViewWrap? {
        return null
    }
    override fun afterInjectView(view: View) {
        setEnableSwipeBack(-1)
        ivPost.setOnClickListener { v->v.animate().withLayer().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction( { v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start() }).start()
            ToastUtile.showToast("ivPost-click()") }

        rb1.setChecked(true)
    }

}
