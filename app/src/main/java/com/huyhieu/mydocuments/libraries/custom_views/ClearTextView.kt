package com.huyhieu.mydocuments.libraries.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class ClearTextView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100F
        textAlign = Paint.Align.CENTER
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) // Xóa chữ khỏi layer
        isFakeBoldText = true // Làm chữ đậm hơn
    }

    private val text = "Tiên Đế Quy Lai"
    private var textWidth = 0f
    private var textHeight = 0f

    init {
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        textWidth = bounds.width().toFloat()
        textHeight = bounds.height().toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (textWidth + 50).toInt() // Thêm padding nhỏ
        val desiredHeight = (textHeight + 50).toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Tạo một layer offscreen
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

        // Vẽ nền đỏ lên layer offscreen
        canvas.drawColor(Color.RED)

        // Vẽ chữ trong suốt (CLEAR mode)
        val x = width / 2f
        val y = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, x, y, textPaint)

        // Đưa layer về canvas chính
        canvas.restoreToCount(layerId)
    }

}
