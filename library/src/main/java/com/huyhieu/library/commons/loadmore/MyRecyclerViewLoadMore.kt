package com.huyhieu.library.commons.loadmore

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewLoadMore(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    var onLoadMore: (() -> Unit)? = null

    init {
        var indexCurrent = 0
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0 && indexCurrent == getItemCount() - 1 && !getEndPage()) {
                    //Add progress LoadMore
                    onLoadMore?.invoke()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                indexCurrent = layoutManager.findLastCompletelyVisibleItemPosition()
            }
        })
    }

    private fun getItemCount() = this@MyRecyclerViewLoadMore.adapter?.itemCount ?: 0

    private fun getEndPage() = (this@MyRecyclerViewLoadMore.adapter as? MyAdapterLoadMore<*>)?.isEndPage ?: false
}