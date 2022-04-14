package com.huyhieu.mydocuments.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T : ViewBinding>(context: Context, @StyleRes themeResId: Int) :
    Dialog(context, themeResId) {
    lateinit var mBinding: T

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun initializeBinding(): DialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
     */
    abstract fun initializeBinding(): T

    abstract fun onReady(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mBinding = initializeBinding()
        setContentView(mBinding.root)
        onReady(savedInstanceState)
    }

}