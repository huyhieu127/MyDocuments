package com.huyhieu.mydocuments.base.interfaces

import android.view.KeyEvent
import android.view.View
import androidx.viewbinding.ViewBinding

interface IViewClickListener<VB : ViewBinding> : View.OnClickListener, IParams<VB> {
    /**
     * View click
     * */
    fun VB.setViewsClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this@IViewClickListener)
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
                        onBackPressed()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    fun onBackPressed() {}
}