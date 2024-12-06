package com.huyhieu.mydocuments.ui.fragments.text_animation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R

class CustomTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val paintBackground = Paint().apply {
        color = ColorUtils.setAlphaComponent(
            ContextCompat.getColor(context, R.color.black),
            (255 * 0.8).toInt() //0.8 is Alpha 0 -> 1
        )
    }
    val paintText = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.transparent)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectFrame = Rect(0, 0, width, height)
        // Draw the background image on the bitmap canvas
        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.dau_thanh_2024)
        //canvas.drawBitmap(backgroundImage, null, rectFrame, null)
        //canvas.drawRect(rectFrame, paintBackground)

        // Draw the text on the bitmap canvas
        //canvas.drawText(text.toString(), x.toFloat(), bottom.toFloat(), paint)
        logDebug("Text: $text --- $left - $top - $right - $bottom")
    }

}