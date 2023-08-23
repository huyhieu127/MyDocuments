package com.huyhieu.mydocuments.libraries.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetMyTagViewBinding
import com.huyhieu.mydocuments.libraries.extensions.color

class MyTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = WidgetMyTagViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.UTagView) {
            val bgColor =
                getColor(
                    R.styleable.UTagView_tagBackground,
                    context.color(R.color.colorSecondary)
                )
            binding.setBackgroundTag(bgColor)

            val icon = getDrawable(R.styleable.UTagView_tagSrcIcon)
            binding.setIcon(icon)

            val title = getString(R.styleable.UTagView_tagTitle)
            binding.setTitle(title)
        }
    }

    private fun WidgetMyTagViewBinding.setIcon(icon: Drawable?) {
        imgIcon.setImageDrawable(icon)
    }

    private fun WidgetMyTagViewBinding.setTitle(title: String?) {
        tvTitle.text = title ?: ""
    }

    private fun WidgetMyTagViewBinding.setBackgroundTag(@ColorInt colorInt: Int) {
        root.setCardBackgroundColor(colorInt)
    }
}