package com.shen.house.fragment4

import android.arch.lifecycle.Observer
import android.view.View
import android.widget.TextView
import com.shen.baselibrary.base.BaseFragment
import com.shen.baselibrary.utiles.LiveDataBus
import com.shen.house.R


class FourthFragment : BaseFragment() {
    override fun getcontentView() = R.layout.fragment_fourth

    override fun afterInjectView(view: View) {
        val button = view.findViewById<TextView>(R.id.button)
        val textView = view.findViewById<TextView>(R.id.textView)
        LiveDataBus.get().with("key_test").setValue(1)
        LiveDataBus.get().with("key_test").setValue(2)
        LiveDataBus.get().with("key_test").setValue(3)
        button.setOnClickListener {
            LiveDataBus.get().with("key_test").setValue(666)
        }
        LiveDataBus.get().with("key_test", Int::class.java)
                .observe(this, object : Observer<Int> {
                    override fun onChanged(aBoolean: Int?) {
//                        ToastUtile.showToast("" + aBoolean)
//                        textView.setText("" + aBoolean)
                        aBoolean?.let { textView.append(" " + aBoolean) }

                    }
                })
    }

    override fun getData() {

    }

}

