package com.huyhieu.mydocuments.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T : ViewBinding>(context: Context, @StyleRes themeResId: Int) :
    AlertDialog(context, themeResId) {
    lateinit var mBinding: T

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun initializeBinding(): DialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
     */
    abstract fun initializeBinding(): T

    abstract fun T.onReady(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = initializeBinding()
        setContentView(mBinding.root)
        mBinding.onReady(savedInstanceState)
    }

    fun setTopDialog() {
        window?.let { window ->
            val wlp: WindowManager.LayoutParams = window.attributes
            wlp.gravity = Gravity.TOP
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
        }
    }
}