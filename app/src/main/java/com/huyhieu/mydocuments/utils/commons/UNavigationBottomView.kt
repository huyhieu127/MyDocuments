package com.huyhieu.mydocuments.utils.commons

import android.annotation.SuppressLint
import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetUNavigationBottomBinding
import com.huyhieu.mydocuments.utils.setAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


enum class UTab {
    TAB_SCAN,
    TAB_HOME,
    TAB_PROFILE
}

class UNavigationBottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding =
        WidgetUNavigationBottomBinding.inflate(LayoutInflater.from(context), this, true)

    private val durationTransaction = 250L

    var tabSelectedListener: ((tab: UTab) -> Unit)? = null

    init {
        with(binding) {
            CoroutineScope(Dispatchers.IO).launch {
                imgTabScan.setOnTouchEvent()
                imgTabHome.setOnTouchEvent()
                imgTabProfile.setOnTouchEvent()
            }
        }
        setTabSelected(UTab.TAB_HOME)
    }

    @SuppressLint("Recycle")
    fun setTabSelected(tab: UTab) {
        with(binding) {
            when (tab) {
                UTab.TAB_SCAN -> {
                    //imgScan.startAnimation(anim)
                    setTabTransaction(imgTabScan.id)
                }
                UTab.TAB_HOME -> {
                    //imgHome.startAnimation(anim)
                    setTabTransaction(imgTabHome.id)
                }
                UTab.TAB_PROFILE -> {
                    //imgProfile.startAnimation(anim)
                    setTabTransaction(imgTabProfile.id)
                }
            }
            imgTabScan.isSelected = tab == UTab.TAB_SCAN
            imgTabHome.isSelected = tab == UTab.TAB_HOME
            imgTabProfile.isSelected = tab == UTab.TAB_PROFILE
        }
        tabSelectedListener?.invoke(tab)
    }

    private fun setTabTransaction(idTab: Int) {
        with(binding) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(root)
            constraintSet.connect(tabSelected.id, ConstraintSet.START, idTab, ConstraintSet.START)
            constraintSet.connect(tabSelected.id, ConstraintSet.END, idTab, ConstraintSet.END)
            val transition = ChangeBounds()
            transition.interpolator = AccelerateDecelerateInterpolator()
            transition.duration = durationTransaction
            TransitionManager.beginDelayedTransition(root, transition)
            constraintSet.applyTo(root)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun View.setOnTouchEvent() {
        val animIn: Animation =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale_bounce_zoom_in)
        val animOut: Animation =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale_bounce_zoom_out)
        this.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.startAnimation(animIn)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    view.startAnimation(animOut)
                    with(binding) {
                        when (view.id) {
                            imgTabScan.id -> {
                                setTabSelected(UTab.TAB_SCAN)
                            }
                            imgTabHome.id -> {
                                setTabSelected(UTab.TAB_HOME)
                            }
                            imgTabProfile.id -> {
                                setTabSelected(UTab.TAB_PROFILE)
                            }
                        }
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    override fun setVisibility(visibility: Int) {
        when (visibility) {
            View.VISIBLE -> {
                if (!isVisible) {
                    super.setVisibility(visibility)
                    this.setAnimation(R.anim.anim_slide_up)
                }
            }
            View.GONE -> {
                if (!isGone) {
                    this.setAnimation(R.anim.anim_slide_down) {
                        super.setVisibility(visibility)
                    }
                }
            }
            else -> super.setVisibility(visibility)
        }
    }
}


