package com.huyhieu.mydocuments.base.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.annotation.LayoutRes
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BaseViewHolder : RecyclerView.ViewHolder {
    constructor(parent: ViewGroup, @LayoutRes layoutId: Int) :
            super(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
}

class GoneViewHolder : RecyclerView.ViewHolder {
    constructor(parent: ViewGroup) :
            super(View(parent.context).also { it.visibility = View.GONE })
}

class ItemOptions(val layoutId: Int = 1, val inflaterInvoker: (View) -> ViewBinding)

typealias OnItemClick<T> = ((T) -> Unit)?

typealias OnItemViewClick<T> = ((T, ViewBinding) -> Unit)?

typealias OnItemPositionClick<T> = ((T, Int) -> Unit)?

open class DiffItemCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }
}

fun RecyclerView.setItemAppearAnimation() {
    val screenHeight = 1080F
    val set = AnimationSet(true)
    set.addAnimation(AlphaAnimation(0.0F, 1.0F).also {
        it.duration = 600
        it.fillAfter = true
    })
    set.addAnimation(TranslateAnimation(0F, 0F, screenHeight, 0F).also {
        it.interpolator = DecelerateInterpolator(4F)
        it.duration = 600
    })
    layoutAnimation = LayoutAnimationController(set, 0.2F)
}

fun RecyclerView.scrollToCenter(position: Int) {
    if (position < 0) return
    findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        delay(300)
        val smoothScroller: RecyclerView.SmoothScroller =
            CenterLayoutManager.CenterSmoothScroller(context)
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }
}

class CenterLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        if (position !in 0 until (recyclerView.adapter?.itemCount ?: 0)) return
        val centerSmoothScroller = CenterSmoothScroller(recyclerView.context)
        centerSmoothScroller.targetPosition = position
        startSmoothScroll(centerSmoothScroller)
    }

    class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int = (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
    }

}