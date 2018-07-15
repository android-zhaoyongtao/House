package com.shen.baselibrary.utiles

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.shen.baselibrary.ContextHouse

object KeyBoardUtils {
    fun toggleKeyBoard(b: Boolean, v: View) {
        val imm = ContextHouse.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (b) {
            imm.showSoftInput(v, 0)
        } else {
            //           if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
            //           }
        }
    }
}