package com.huyhieu.mydocuments.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialog<T : ViewBinding>(context: Context, @StyleRes themeResId: Int) :
    BottomSheetDialog(context, themeResId), IBaseViewDialog<T> {
    lateinit var vb: T

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun binding(): DialogConfirmBinding = DialogConfirmBinding.inflate(layoutInflater)
     */
    abstract fun binding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        vb = binding()
        setContentView(vb.root)
        onMyViewCreated(savedInstanceState)
    }

}