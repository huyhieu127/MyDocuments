package com.huyhieu.widget.commons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.huyhieu.library.extensions.backgroundTint
import com.huyhieu.library.extensions.dimen
import com.huyhieu.library.extensions.tintVector
import com.huyhieu.widget.R
import com.huyhieu.widget.databinding.WidgetUOptionBinding

class UOptionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding =
        WidgetUOptionBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.UOptionView) {
            val title = getString(R.styleable.UOptionView_optTitle)
            val titleColor = getColor(R.styleable.UOptionView_optTitleColor, 0)
            setTextTitle(title, titleColor)

            val content = getString(R.styleable.UOptionView_optContent)
            val contentColor = getColor(R.styleable.UOptionView_optContentColor, 0)
            setTextContent(content, contentColor)

            val value = getString(R.styleable.UOptionView_optValue)
            val valueColor = getColor(R.styleable.UOptionView_optValueColor, 0)
            setTextValue(value, valueColor)

            val valueFillBgColor =
                getColorStateList(R.styleable.UOptionView_optValueFillBgColor)
            val valueFill = getString(R.styleable.UOptionView_optValueFill)
            val valueFillColor = getColor(R.styleable.UOptionView_optValueFillColor, 0)
            setTextValueFill(valueFill, valueFillColor, valueFillBgColor)


            val icon = getDrawable(R.styleable.UOptionView_optSrcIcon)
            val tintIcon = getColor(R.styleable.UOptionView_optTintIcon, 0)
            val bgIcon = getDrawable(R.styleable.UOptionView_optBgDrawableIcon)
            val bgIconColor = getColorStateList(R.styleable.UOptionView_optBgColorIcon)
            setIcon(icon, tintIcon, bgIcon, bgIconColor)

            val arrowRight = getDrawable(R.styleable.UOptionView_optSrcArrowRight)
            val tintArrowRight = getColor(R.styleable.UOptionView_optTintArrowRight, 0)
            setArrowRight(arrowRight, tintArrowRight)
        }
    }

    fun setArrowRight(icon: Drawable?, @ColorInt tintArrowRight: Int) {
        with(binding) {
            imgArrowRight.isVisible = icon != null
            if (icon != null) {
                imgArrowRight.setImageDrawable(icon)
            }
            if (tintArrowRight != 0) {
                imgArrowRight.tintVector(tintArrowRight)
            }
        }
    }

    fun setIcon(
        icon: Drawable?,
        @ColorInt tintIcon: Int = 0,
        bgIcon: Drawable?,
        colorStateList: ColorStateList?
    ) {
        with(binding) {
            imgIcon.isVisible = icon != null
            if (icon != null) {
                imgIcon.setImageDrawable(icon)
            }
            if (tintIcon != 0) {
                imgIcon.tintVector(tintIcon)
            }
            if (bgIcon != null) {
                vBgIcon.background = bgIcon
            }
            vBgIcon.backgroundTint(colorStateList)
        }
    }

    fun setTextTitle(title: String?, @ColorInt idColor: Int = 0) {
        with(binding) {
            tvTitle.isVisible = !title.isNullOrEmpty()
            if (!title.isNullOrEmpty()) {
                tvTitle.text = title
            }
            if (idColor != 0) {
                tvTitle.setTextColor(idColor)
            }
        }
    }

    fun setTextContent(content: String?, @ColorInt idColor: Int = 0) {
        with(binding) {
            val hasContent = content.isNullOrEmpty()
            tvContent.isVisible = !hasContent
            //Clear margin title
            val lp = tvTitle.layoutParams as ConstraintLayout.LayoutParams
            val marginBottom = context.dimen(R.dimen.margin_bottom_title_option_view)
            lp.bottomMargin = if (!hasContent) marginBottom.toInt() else 0
            tvTitle.layoutParams = lp

            if (!hasContent) {
                tvContent.text = content
            }
            if (idColor != 0) {
                tvContent.setTextColor(idColor)
            }
        }
    }

    fun setTextValue(value: String?, @ColorInt idColor: Int = 0) {
        with(binding) {
            tvValue.isVisible = !value.isNullOrEmpty()
            if (!value.isNullOrEmpty()) {
                tvValue.text = value
            }
            if (idColor != 0) {
                tvValue.setTextColor(idColor)
            }
        }
    }

    fun setTextValueFill(
        value: String?,
        @ColorInt idColor: Int = 0,
        colorStateList: ColorStateList?
    ) {
        with(binding) {
            tvValueFill.isVisible = !value.isNullOrEmpty()
            if (!value.isNullOrEmpty()) {
                tvValueFill.text = value
            }
            if (idColor != 0) {
                tvValueFill.setTextColor(idColor)
            }
            if (colorStateList != null) {
                tvValueFill.backgroundTint(colorStateList)
            }
        }
    }

    override fun setPressed(pressed: Boolean) {
        alpha = if (pressed) 0.5F else 1F
        super.setPressed(pressed)
    }
}