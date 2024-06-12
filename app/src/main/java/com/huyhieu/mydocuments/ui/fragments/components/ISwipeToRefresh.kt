package com.huyhieu.mydocuments.ui.fragments.components

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.libraries.extensions.animateHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface ISwipeToRefresh {
    var isRefreshing: Boolean

    @SuppressLint("ClickableViewAccessibility")
    fun SwipeToRefreshFragment.setupRecyclerView() = with(vb) {
        resetLoadingViewHeight()
        rcvNotifications.adapter = adapter
        rcvNotifications.isNestedScrollingEnabled = false
        var isDragging = false
        var initialY = 0f
        //For EMA
        var lastDistance = 0f
        val smoothingFactor = 0.3f
        rcvNotifications.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDragging = !isRefreshing
                    initialY = event.y
                    lastDistance = 0F// if (!isRefreshing) 0f else flRefresh.height.toFloat()
                    if (isRefreshing) {
                        setHiddenRefresh()
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val isCanRefresh = isDragging && !rcvNotifications.canScrollVertically(-1)
                    if (isCanRefresh) {
                        val distance = event.y - initialY
                        logDebug("$isCanRefresh - $isRefreshing : $distance = ${event.y} - $initialY")
                        if (distance > 0) {
                            // Apply Exponential Moving Average (EMA)
                            val smoothedDistance =
                                lastDistance * (1 - smoothingFactor) + distance * smoothingFactor
                            lastDistance = smoothedDistance
                            setLoadingViewHeight(lastDistance.toInt())
                        }
                    } else if (isRefreshing) {
                        val distance = event.y - initialY
                        // Apply Exponential Moving Average (EMA)
                        val smoothedDistance =
                            lastDistance * (1 - smoothingFactor) + distance * smoothingFactor
                        lastDistance = smoothedDistance
                        setLoadingViewHeight(lastDistance.toInt())
                        return@setOnTouchListener true
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDragging = false
                    showLoadingViewHeight(lastDistance)
                }
            }
            false
        }
    }


    private fun SwipeToRefreshFragment.setLoadingViewHeight(toHeight: Int) = with(vb) {
        flRefresh.updateLayoutParams<ViewGroup.LayoutParams> {
            height = toHeight
        }
        flRefresh.isVisible = true
        if (toHeight <= 0) isRefreshing = false
    }

    private fun SwipeToRefreshFragment.resetLoadingViewHeight() = with(vb) {
        flRefresh.updateLayoutParams<ViewGroup.LayoutParams> {
            height = 0
        }
        flRefresh.isVisible = false
        isRefreshing = false
    }

    private fun SwipeToRefreshFragment.showLoadingViewHeight(distance: Float) = with(vb) {
        val viewRefreshHeight = prgRefresh.height
        val isVisible = distance >= viewRefreshHeight

        flRefresh.animateHeight(if (isVisible) viewRefreshHeight else 0)
        if (!isVisible) {
            lifecycleScope.launch {
                delay(300)
                flRefresh.isVisible = false
            }
        } else {
            flRefresh.isVisible = true
        }

        isRefreshing = isVisible
        if (isVisible) {
            onRefreshing(true)
        }
    }

    fun SwipeToRefreshFragment.setHiddenRefresh() {
        isRefreshing = false
        //onRefreshing(false)
        showLoadingViewHeight(0F)
    }

    fun onRefreshing(isRefreshing: Boolean)
}