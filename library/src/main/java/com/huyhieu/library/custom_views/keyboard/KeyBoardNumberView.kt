package com.huyhieu.library.custom_views.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.huyhieu.library.databinding.WidgetKeyboardNumberBinding

class KeyBoardNumberView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val vb = WidgetKeyboardNumberBinding.inflate(LayoutInflater.from(context), this, true)
    private val keyAdapter by lazy { KeyAdapter() }
    private val keys by lazy {
        mutableListOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "DEL"
        )
    }

    var outputCallback: ((value: String) -> Unit)? = null
    var value = StringBuilder("")

    init {
        vb.root.adapter = keyAdapter
        keyAdapter.fillData(keys)
        keyAdapter.keyClick = {
            if (it != "DEL") {
                value.append(it)
            } else if (value.isNotEmpty()) {
                value.deleteAt(value.length - 1)
            }
            outputCallback?.invoke(value.toString())
        }
        vb.root.post {
            val duration = 250L
            vb.root.animate().translationY(0F).setDuration(duration).start()
            vb.root.animate().alpha(1F).setDuration(duration).start()
        }
    }

    fun clear() {
        value.clear()
    }
}