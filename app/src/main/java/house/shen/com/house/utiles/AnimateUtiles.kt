package house.shen.com.house.utiles

import android.view.MotionEvent
import android.view.View

object AnimateUtiles {
    fun handleTouch(v: View, event: MotionEvent, runnable: Runnable?): Boolean {
        val animate = v.animate().withLayer().setDuration(200)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animate.setListener(null)
                v.setTag(v.id, false)//tag意义:up时是否放大动画
                animate.scaleX(1.1f).scaleY(1.1f).withEndAction {
                    v.setTag(v.id, true)
                    if (event.action === MotionEvent.ACTION_UP || event.action === MotionEvent.ACTION_CANCEL) {
                        animate.scaleX(1f).scaleY(1f).withStartAction { v.setTag(v.id, false) }.withEndAction {
                            animate.setListener(null)
                            runnable?.run()
                        }.start()
                    }
                }.start()
            }
            MotionEvent.ACTION_UP -> {
                if (v.getTag(v.id) as Boolean) {
                    animate.scaleX(1f).scaleY(1f).withEndAction {
                        animate.setListener(null)
                        runnable?.run()
                    }.start()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                animate.scaleX(1f).scaleY(1f).start()
            }
        }
        return@handleTouch true
    }
}
