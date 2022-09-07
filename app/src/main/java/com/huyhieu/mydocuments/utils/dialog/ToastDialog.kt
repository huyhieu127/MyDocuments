package com.huyhieu.mydocuments.utils.dialog

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseDialogFragment
import com.huyhieu.mydocuments.databinding.DialogToastBinding
import com.huyhieu.mydocuments.utils.extensions.backgroundTint
import com.huyhieu.mydocuments.utils.extensions.color
import com.huyhieu.mydocuments.utils.extensions.drawable
import com.huyhieu.mydocuments.utils.extensions.tintVector

enum class ToastType {
    NORMAL,
    SUCCESS,
    FAILED,
    OTHER
}

class ToastDialog(
    val type: ToastType,
    val title: String? = "",
    val content: String? = "",
    val iconDrawable: Int? = null,
    val bgDrawable: Int? = null,
    val iconColor: Int? = null,
    val bgIconColor: Int? = null,
) : BaseDialogFragment<DialogToastBinding>() {

    override fun initializeBinding() = DialogToastBinding.inflate(layoutInflater)

    override fun DialogToastBinding.onReady(savedInstanceState: Bundle?) {
        setTopDialog()
        setTouchBehindDialog()
        createUI()

        cvCancel.setOnClickListener(this@ToastDialog)
    }

    private fun DialogToastBinding.createUI() {
        when (type) {
            ToastType.NORMAL -> {
                root.background = context?.drawable(R.drawable.bg_outline_gray_white_corner_12)
                imgIcon.backgroundTint(R.color.colorPrimary)
                imgIcon.tintVector(context?.color(R.color.black)!!)
            }
            ToastType.SUCCESS -> {
                root.background = context?.drawable(R.drawable.bg_outline_green_mint_corner_12)
                imgIcon.backgroundTint(R.color.colorGreenMint)
                imgIcon.tintVector(context?.color(R.color.white)!!)
            }
            ToastType.FAILED -> {
                root.background = context?.drawable(R.drawable.bg_outline_red_light_corner_12)
                imgIcon.backgroundTint(R.color.colorRedAlert)
                imgIcon.tintVector(context?.color(R.color.white)!!)
            }
            else -> {
                if (bgDrawable != null) {
                    root.background = context?.drawable(bgDrawable)
                }
                if (bgIconColor != null) {
                    imgIcon.backgroundTint(bgIconColor)
                }
                if (iconColor != null) {
                    imgIcon.tintVector(context?.color(iconColor)!!)
                }
            }
        }
        if (iconDrawable != null) {
            imgIcon.setImageDrawable(context?.drawable(iconDrawable))
        }
        if (!title.isNullOrEmpty()) {
            tvTitle.text = title
        }
        if (!content.isNullOrEmpty()) {
            tvContent.text = content
        }
    }

    override fun DialogToastBinding.onClickViewBinding(v: View) {
        when (v.id) {
            cvCancel.id -> dismissAllowingStateLoss()
        }
    }
}