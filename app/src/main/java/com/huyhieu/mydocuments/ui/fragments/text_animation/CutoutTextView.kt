package com.huyhieu.mydocuments.ui.fragments.text_animation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.huyhieu.mydocuments.R

class CutoutTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 200f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the text
        val text = "Hello World"
        val x = width / 2f
        val y = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2

        canvas.drawText(text, x, y, textPaint)

        // Create a bitmap with the same size as the canvas
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val bitmapCanvas = Canvas(bitmap)

        // Draw the background image on the bitmap canvas
        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.dau_thanh_2024)
        bitmapCanvas.drawBitmap(backgroundImage, null, Rect(0, 0, width, height), null)

        // Draw the text on the bitmap canvas to cut out the background
        bitmapCanvas.drawText(text, x, y, backgroundPaint)

        // Draw the resulting bitmap on the original canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }
}
