package com.huyhieu.mydocuments.base.interfaces

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewbinding.ViewBinding
import com.huyhieu.library.utils.logDebug

interface IViewClickListener<VB : ViewBinding> : View.OnClickListener, IParams<VB> {
    /**
     * View click
     * */
    fun setClickViews(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this@IViewClickListener)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun MotionLayout.setClickViewsOfMotion(vararg views: View) {
        views.forEach {
            it.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP && (progress == 0.0F || progress == 1.0F)) {
                    logDebug(this.transitionName)
                    vb.onClickViewBinding(v, v.id)
                }
                return@setOnTouchListener false
            }
        }
    }

    fun VB.onClickViewBinding(v: View, id: Int) {}

    /**
     * Back device
     * @param View is root view
     * */
    fun View?.handleBackDevice() {
        this?.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        onMyBackPressed()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    fun onMyBackPressed() {}
}