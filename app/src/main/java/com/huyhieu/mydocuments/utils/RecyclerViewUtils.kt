package com.huyhieu.mydocuments.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.init(
    adapter: RecyclerView.Adapter<*>,
    layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false),
    hasFixedSize: Boolean = true,
    onSetupLayoutManager: (RecyclerView.LayoutManager.() -> Unit)? = null
) {
    this.adapter = adapter
    this.setHasFixedSize(hasFixedSize)

    onSetupLayoutManager?.invoke(layoutManager)
    this.layoutManager = layoutManager
}