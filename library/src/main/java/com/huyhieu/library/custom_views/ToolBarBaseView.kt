package com.huyhieu.library.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.huyhieu.library.R
import com.huyhieu.library.databinding.WidgetToolbarBaseBinding

class ToolBarBaseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var vb = WidgetToolbarBaseBinding.inflate(LayoutInflater.from(context), this, true)
    var title: String
        get() {
            return vb.tvTitle.text.toString()
        }
        set(value) {
            vb.tvTitle.text = value
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.ToolBarBaseView) {
            val textTitle = getString(R.styleable.ToolBarBaseView_tb_base_title) ?: ""
            this@ToolBarBaseView.title = textTitle
        }
    }
}