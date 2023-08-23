package com.huyhieu.mydocuments.libraries.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetMyNavigationBottomBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.utils.setAnimation
import eightbitlab.com.blurview.BlurAlgorithm
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


enum class UTab {
    TAB_SCAN,
    TAB_HOME,
    TAB_PROFILE
}

class MyNavigationBottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val vb =
        WidgetMyNavigationBottomBinding.inflate(LayoutInflater.from(context), this, true)

    private val durationTransaction = 250L

    var tabSelectedListener: ((tab: UTab) -> Unit)? = null

    init {
        with(vb) {
            CoroutineScope(Dispatchers.IO).launch {
                /*imgTabScan.setOnTouchEvent()
                imgTabHome.setOnTouchEvent()
                imgTabProfile.setOnTouchEvent()*/
            }
            setGesture()
        }
        setTabSelected(UTab.TAB_HOME)
    }

    fun setBackgroundBlur(viewTargetParent: ViewGroup) {
        val radius = 20F
        vb.blBackground.setupWith(viewTargetParent, getBlurAlgorithm())
            .setFrameClearDrawable(vb.blBackground.background)
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setOverlayColor(context.color(R.color.black_10))
    }

    private fun getBlurAlgorithm(): BlurAlgorithm {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RenderEffectBlur()
        } else {
            RenderScriptBlur(context)
        }
    }

    private fun WidgetMyNavigationBottomBinding.setGesture() {
        imgTabScan.setOnClickListener(this@MyNavigationBottomView)
        imgTabHome.setOnClickListener(this@MyNavigationBottomView)
        imgTabProfile.setOnClickListener(this@MyNavigationBottomView)
    }

    @SuppressLint("Recycle")
    fun setTabSelected(tab: UTab) {
        with(vb) {
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
        with(vb) {
            cstNavigation.post {
                val constraintSet = ConstraintSet()
                constraintSet.clone(cstNavigation)
                constraintSet.connect(
                    tabSelected.id,
                    ConstraintSet.START,
                    idTab,
                    ConstraintSet.START
                )
                constraintSet.connect(tabSelected.id, ConstraintSet.END, idTab, ConstraintSet.END)
                val transition = ChangeBounds()
                transition.interpolator = AccelerateDecelerateInterpolator()
                transition.duration = durationTransaction
                TransitionManager.beginDelayedTransition(cstNavigation, transition)
                constraintSet.applyTo(cstNavigation)
            }
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
                    with(vb) {
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

    override fun onClick(v: View?) {
        with(vb) {
            when (v?.id) {
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


