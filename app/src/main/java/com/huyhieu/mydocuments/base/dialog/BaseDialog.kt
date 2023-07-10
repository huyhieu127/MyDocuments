package com.huyhieu.mydocuments.base.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<VB : ViewBinding>(context: Context, @StyleRes themeResId: Int) :
    AlertDialog(context, themeResId), IBaseViewDialog<VB>  {
    lateinit var vb: VB

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun binding(): DialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
     */
    abstract fun binding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = binding()
        setContentView(vb.root)
        onMyViewCreated(savedInstanceState)
    }

    fun setAnchorTop() {
        window?.let { window ->
            val wlp: WindowManager.LayoutParams = window.attributes
            wlp.gravity = Gravity.TOP
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
        }
    }

    fun setAllowTouchBehind() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
    }
}