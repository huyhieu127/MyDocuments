package com.huyhieu.library.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.huyhieu.library.R
import com.huyhieu.library.databinding.WidgetMyNoteBinding
import com.huyhieu.library.extensions.backgroundTint
import com.huyhieu.library.extensions.dimen
import com.huyhieu.library.extensions.tintVector

class MyNoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = WidgetMyNoteBinding.inflate(LayoutInflater.from(context), this, true)

    val tvSeeMore get() = binding.tvSeeMore

    init {
        context.withStyledAttributes(attrs, R.styleable.UNoteView) {
            val title = getString(R.styleable.UNoteView_noteTitle)
            val titleColor = getColor(R.styleable.UNoteView_noteTitleColor, 0)
            setTextTitle(title, titleColor)

            val content = getString(R.styleable.UNoteView_noteContent)
            val contentColor = getColor(R.styleable.UNoteView_noteContentColor, 0)
            setTextContent(content, contentColor)

            val seeMore = getString(R.styleable.UNoteView_noteSeeMore)
            val seeMoreColor = getColor(R.styleable.UNoteView_noteSeeMoreColor, 0)
            setTextSeeMore(seeMore, seeMoreColor)
            setGestureSeeMore()

            val icon = getDrawable(R.styleable.UNoteView_noteSrcIcon)
            val tintIcon = getColor(R.styleable.UNoteView_noteTintIcon, 0)
            val bgIcon = getDrawable(R.styleable.UNoteView_noteBgDrawableIcon)
            val bgIconColor = getColorStateList(R.styleable.UNoteView_noteBgColorIcon)
            setIcon(icon, tintIcon, bgIcon, bgIconColor)

            val bg = getDrawable(R.styleable.UNoteView_noteBgDrawable)
            val bgColor = getColorStateList(R.styleable.UNoteView_noteBgColor)
            setRoot(bg, bgColor)
        }
    }

    fun setRoot(
        bgIcon: Drawable?,
        colorStateList: ColorStateList?
    ) {
        with(binding) {
            if (bgIcon != null) {
                root.background = bgIcon
            }
            root.backgroundTint(colorStateList)
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
                imgIcon.background = bgIcon
            }
            imgIcon.backgroundTint(colorStateList)
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
            val marginBottom = context.dimen(R.dimen.margin_bottom_title_note_view)
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

    fun setTextSeeMore(value: String?, @ColorInt idColor: Int = 0) {
        with(binding) {
            tvSeeMore.isVisible = !value.isNullOrEmpty()
            if (!value.isNullOrEmpty()) {
                tvSeeMore.text = value
            }
            if (idColor != 0) {
                tvSeeMore.setTextColor(idColor)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setGestureSeeMore() {
        with(binding) {
            tvSeeMore.setOnTouchListener { view, event ->
                if (tvSeeMore.hasOnClickListeners()) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            tvSeeMore.alpha = 0.5F
                            return@setOnTouchListener false
                        }
                    }
                }
                tvSeeMore.alpha = 1F
                return@setOnTouchListener false
            }
        }
    }


    override fun setPressed(pressed: Boolean) {
        alpha = if (pressed) 0.75F else 1F
        super.setPressed(pressed)
    }
}