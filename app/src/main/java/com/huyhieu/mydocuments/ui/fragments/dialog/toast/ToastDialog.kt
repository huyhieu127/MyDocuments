package com.huyhieu.mydocuments.ui.fragments.dialog.toast

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.libraries.extensions.backgroundTint
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.drawable
import com.huyhieu.mydocuments.libraries.extensions.tintVector
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.dialog.BaseDialogFragment
import com.huyhieu.mydocuments.databinding.DialogToastBinding

enum class ToastType {
    NORMAL,
    SUCCESS,
    FAILED,
    OTHER
}

class ToastDialog(
    val type: ToastType,
    var title: String? = "",
    var content: String? = "",
    val iconDrawable: Int? = null,
    val bgDrawable: Int? = null,
    val iconColor: Int? = null,
    val bgIconColor: Int? = null,
    var onClose: ((ToastDialog) -> Unit)? = null
) : BaseDialogFragment<DialogToastBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        setAnchorTop()
        setAllowTouchBehind()
        createUI()

        cvCancel.setOnClickListener(this@ToastDialog)
    }

    private fun DialogToastBinding.createUI() {
        when (type) {
            ToastType.NORMAL -> {
                root.background =
                    context?.drawable(R.drawable.bg_outline_gray_white_corner_12)
                imgIcon.backgroundTint(R.color.colorPrimary)
                imgIcon.tintVector(context?.color(R.color.black)!!)
            }
            ToastType.SUCCESS -> {
                root.background =
                    context?.drawable(R.drawable.bg_outline_green_mint_corner_12)
                imgIcon.backgroundTint(R.color.colorGreenMint)
                imgIcon.tintVector(context?.color(R.color.white)!!)
            }
            ToastType.FAILED -> {
                root.background =
                    context?.drawable(R.drawable.bg_outline_red_light_corner_12)
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

    fun updateData(title: String? = this.title, content: String? = this.content) {
        if (title != null && this.title != title) {
            this.title = title
            vb.tvTitle.text = title
        }
        if (content != null && this.content != content) {
            this.content = content
            vb.tvContent.text = content
        }
    }

    override fun DialogToastBinding.onClickViewBinding(v: View, id: Int) {
        when (v.id) {
            cvCancel.id -> {
                onClose?.invoke(this@ToastDialog) ?: dismissAllowingStateLoss()
            }
        }
    }
}
