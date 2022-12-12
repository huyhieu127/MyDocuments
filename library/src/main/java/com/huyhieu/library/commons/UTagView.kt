package com.huyhieu.library.commons

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.huyhieu.library.R
import com.huyhieu.library.databinding.WidgetUTagViewBinding
import com.huyhieu.library.extensions.color

class UTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = WidgetUTagViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.UTagView) {
            val bgColor =
                getColor(
                    R.styleable.UTagView_tagBackground,
                    context.color(com.huyhieu.library.R.color.colorSecondary)
                )
            binding.setBackgroundTag(bgColor)

            val icon = getDrawable(R.styleable.UTagView_tagSrcIcon)
            binding.setIcon(icon)

            val title = getString(R.styleable.UTagView_tagTitle)
            binding.setTitle(title)
        }
    }

    private fun WidgetUTagViewBinding.setIcon(icon: Drawable?) {
        imgIcon.setImageDrawable(icon)
    }

    private fun WidgetUTagViewBinding.setTitle(title: String?) {
        tvTitle.text = title ?: ""
    }

    private fun WidgetUTagViewBinding.setBackgroundTag(@ColorInt colorInt: Int) {
        root.setCardBackgroundColor(colorInt)
    }
}