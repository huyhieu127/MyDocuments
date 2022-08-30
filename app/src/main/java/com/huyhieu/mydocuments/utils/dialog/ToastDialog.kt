package com.huyhieu.mydocuments.utils.dialog

import android.content.Context
import android.os.Bundle
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseDialog
import com.huyhieu.mydocuments.databinding.DialogToastBinding


class ToastDialog(private val ctx: Context) :
    BaseDialog<DialogToastBinding>(ctx, R.style.CustomAlertDialog) {
    override fun initializeBinding() = DialogToastBinding.inflate(layoutInflater)

    override fun DialogToastBinding.onReady(savedInstanceState: Bundle?) {
        setTopDialog()
        cvCancel.setOnClickListener {
            dismiss()
        }
    }
}