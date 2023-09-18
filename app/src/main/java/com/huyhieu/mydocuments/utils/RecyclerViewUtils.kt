package com.huyhieu.mydocuments.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.init(
    adapter: RecyclerView.Adapter<*>,
    layoutManager: RecyclerView.LayoutManager? = null,
    hasFixedSize: Boolean = true,
    onSetupLayoutManager: (RecyclerView.LayoutManager.() -> Unit)? = null
) {
    this.adapter = adapter
    this.setHasFixedSize(hasFixedSize)


    val lm = layoutManager?: this.layoutManager ?:  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    onSetupLayoutManager?.invoke(lm)
    this.layoutManager = lm
}