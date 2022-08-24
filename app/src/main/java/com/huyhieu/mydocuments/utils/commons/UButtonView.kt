package com.huyhieu.mydocuments.utils.commons

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleObserver
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetUButtonBinding
import com.huyhieu.mydocuments.utils.extensions.color
import com.huyhieu.mydocuments.utils.extensions.dimen
import com.huyhieu.mydocuments.utils.extensions.drawable
import com.huyhieu.mydocuments.utils.extensions.typedValue


class UButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), LifecycleObserver {
    private var binding = WidgetUButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var contentType = 0

    @ColorInt
    private var bgColor = 0

    @Dimension
    private var radiusValue = 0F

    @Dimension
    private var elevationValue = 0F

    private var isEffectAlpha = false
    private var isEnableLoading = true
    private var isLoading = false

    companion object {

        private const val CONTENT_TYPE_ICON = 0
        private const val CONTENT_TYPE_TEXT = 1

        private const val EFFECT_CLICK_ALPHA = 0
        private const val EFFECT_CLICK_RIPPLE = 1
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.UButtonView) {
            val isEnable = getBoolean(R.styleable.UButtonView_uEnabled, true)
            this@UButtonView.isEnabled = isEnable

            val isEnableLoading = getBoolean(R.styleable.UButtonView_uEnableLoading, true)
            this@UButtonView.isEnableLoading = isEnableLoading

            val bgColor =
                getColor(R.styleable.UButtonView_uBackground, context.color(R.color.colorPrimary))
            this@UButtonView.bgColor = bgColor

            val elevation = getDimension(
                R.styleable.UButtonView_uElevation,
                context.dimen(R.dimen.btn_elevation)
            )
            this@UButtonView.elevationValue = elevation

            val radius = getDimension(R.styleable.UButtonView_uRadius, 0F)
            this@UButtonView.radiusValue = radius

            val name = getString(R.styleable.UButtonView_uButtonName)
            setText(name)

            val drawable = getDrawable(R.styleable.UButtonView_uIcon)
            setIcon(name, drawable)

            val effect = getInteger(R.styleable.UButtonView_uEffectClick, EFFECT_CLICK_ALPHA)
            setEffectClick(effect)
        }
        hideLoading()

    }

    private fun setText(name: String?) {
        with(binding) {
            if (!name.isNullOrEmpty()) {
                contentType = CONTENT_TYPE_TEXT
                tvName.text = name
                if (radiusValue == 0F) {
                    radiusValue = context.dimen(R.dimen.btn_text_radius)
                }
            }
        }
    }

    private fun setIcon(name: String?, drawable: Drawable?) {
        with(binding) {
            if (name.isNullOrEmpty()) {
                contentType = CONTENT_TYPE_ICON
                if (drawable != null) {
                    imgIcon.setImageDrawable(drawable)
                }
                if (radiusValue == 0F) {
                    radiusValue = context.dimen(R.dimen.btn_icon_radius)
                }
            }
        }
    }


    private fun setEffectClick(effect: Int) {
        when (effect) {
            EFFECT_CLICK_ALPHA -> {
                isEffectAlpha = true
            }
            EFFECT_CLICK_RIPPLE -> {
                foreground =
                    context.drawable(context.typedValue(android.R.attr.selectableItemBackground).resourceId)
            }
        }
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        if (isEffectAlpha) {
            this.alpha = if (pressed) 0.5F else 1F
        }
        /*Effect press down*/
        if (elevationValue != 0F) {
            translationY = if (pressed) {
                elevationValue / 2
            } else {
                0F
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) {
            setUIParent()
            1F
        } else {
            hideLoading()
            0.7F
        }
    }

    private fun setUIParent(
        @ColorInt bgColor: Int = this.bgColor,
        @Dimension radius: Float = radiusValue,
    ) {
        this.setCardBackgroundColor(
            when {
                isLoading -> context.color(android.R.color.transparent)
                !isEnabled -> context.color(R.color.disable)
                else -> bgColor
            }
        )

        this.radius = radius
        this.cardElevation = if (isLoading) 0F else elevationValue
    }

    fun setLoading(isShow: Boolean = false) {
        if (isShow) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    fun showLoading() {
        with(binding) {
            if (isEnableLoading) {
                isLoading = true

                imgIcon.isVisible = false
                tvName.isVisible = false
                imgLoading.isVisible = true

                setUIParent()
            }
        }
    }

    fun hideLoading() {
        with(binding) {
            isLoading = false

            imgIcon.isVisible = contentType == CONTENT_TYPE_ICON
            tvName.isVisible = contentType == CONTENT_TYPE_TEXT

            imgLoading.isVisible = false
            setUIParent()
        }
    }

    fun setOnClickListenerCustom(isEnableLoading: Boolean = true, listener: OnClickListener?) {
        this.isEnableLoading = isEnableLoading
        setOnClickListener(listener)
    }
}