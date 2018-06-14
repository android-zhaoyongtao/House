package house.shen.com.house

import android.view.View
import house.shen.com.house.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun getcontentView(): Int {
        return R.layout.activity_main
    }

    override fun afterInjectView(view: View) {
    }

}
