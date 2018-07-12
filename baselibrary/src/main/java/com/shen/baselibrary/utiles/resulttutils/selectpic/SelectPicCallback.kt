package com.shen.baselibrary.utiles.resulttutils.selectpic

import com.luck.picture.lib.entity.LocalMedia

interface SelectPicCallback {
    fun selectPicResult(list: List<LocalMedia>)
}