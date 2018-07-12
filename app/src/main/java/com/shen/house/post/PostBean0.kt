package com.shen.house.post

import android.util.Pair

data class PostBean0(
        var title: String? = null,
        var content: String? = null,
        var pics: List<String>? = null,
        var name: String? = null,
        var area: Pair<String, String>? = null,
        var address: String? = null,
        var test: Int = 0
)
