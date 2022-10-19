package com.huyhieu.mydocuments.utils.commons

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetUCheckboxBinding
import com.huyhieu.mydocuments.utils.extensions.applySpannable

class UCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = WidgetUCheckboxBinding.inflate(LayoutInflater.from(context), this, true)

    var checkBoxListener: ((isChecked: Boolean) -> Unit)? = null
    var isChecked: Boolean
        get() = binding.imgIcon.isSelected
        set(value) {
            binding.imgIcon.isSelected = value
            checkBoxListener?.invoke(value)
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.UCheckBox) {
            val isChecked = getBoolean(R.styleable.UCheckBox_ckbChecked, false)
            this@UCheckBox.isChecked = isChecked
            setGestureChecked()

            val content = getString(R.styleable.UCheckBox_ckbContent)
            val contentColor = getColor(R.styleable.UCheckBox_ckbContentColor, 0)
            setTextContent(content, contentColor)
        }
    }

    private fun setGestureChecked() {
        with(binding) {
            imgIcon.setOnClickListener {
                this@UCheckBox.isChecked = (!imgIcon.isSelected)
            }
        }

    }

    fun setTextContent(content: Any?, @ColorInt idColor: Int = 0) {
        with(binding) {
            val hasContent = content.toString().isEmpty()
            tvContent.isVisible = !hasContent
            //Clear margin title
            if (!hasContent) {
                if (content is SpannableString) {
                    tvContent.applySpannable(content)
                }
                if (content is String) {
                    tvContent.text = content
                }
            }
            if (idColor != 0) {
                tvContent.setTextColor(idColor)
            }
        }
    }
}