package com.huyhieu.mydocuments.ui.fragments.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.huyhieu.library.R
import com.huyhieu.library.extensions.color
import kotlin.math.cos
import kotlin.math.sin

class RadarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val wView get() = measuredWidth.toFloat()
    private val hView get() = measuredHeight.toFloat()
    private val xCenter get() = wView / 2
    private val yCenter get() = hView / 2
    private val radius get() = wView / 2

    private val numberRow = 5
    private val spaceRow get() = radius / numberRow

    private val paintAxis = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2F
    }

    private val paintPoint = Paint().apply {
        isDither = true
        style = Paint.Style.FILL
        color = context.color(R.color.colorPrimary)
        isAntiAlias = true
        strokeWidth = 20F
        setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
    }

    private val values
        get() = mutableMapOf(
            1 to 1.0F, 2 to 0.75F, 3 to 1F, 4 to 0.75F, 5 to 0.8F, 6 to 0.1F
        )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        drawFrame(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        //drawFrameCircle(canvas)
        (1..numberRow).forEach {
            drawFrameLine(canvas, it)
        }
        drawLinePrimary(canvas)
    }

    private fun drawFrameCircle(canvas: Canvas) {
        val valueStrokeWidth = 4F
        val rect = RectF(
            0F + valueStrokeWidth,
            0F + valueStrokeWidth,
            wView - valueStrokeWidth,
            hView - valueStrokeWidth
        )
        val paint = Paint().apply {
            isDither = true
            style = Paint.Style.STROKE
            color = context.color(R.color.colorPrimaryLight)
            strokeWidth = valueStrokeWidth
            isAntiAlias = true
        }
        canvas.drawArc(rect, 270F, 360F, false, paint)
    }

    private fun drawFrameLine(canvas: Canvas, numericalOther: Int) {
        val corner = 60.0
        val radius = spaceRow * numericalOther
        (1..6).forEach {
            val startX: Float =
                (cos(Math.toRadians(((it) * corner) + 30)).toFloat() * radius) + xCenter
            val startY: Float =
                (sin(Math.toRadians(((it) * corner) + 30)).toFloat() * radius) + yCenter

            val endX: Float =
                cos(Math.toRadians(((it + 1) * corner) + 30)).toFloat() * radius + xCenter
            val endY: Float =
                sin(Math.toRadians(((it + 1) * corner) + 30)).toFloat() * radius + yCenter

            val endXFrame: Float =
                cos(Math.toRadians(((it + 3) * corner) + 30)).toFloat() * radius + xCenter
            val endYFrame: Float =
                sin(Math.toRadians(((it + 3) * corner) + 30)).toFloat() * radius + yCenter

            canvas.drawLine(startX, startY, endX, endY, paintAxis)
            canvas.drawLine(startX, startY, endXFrame, endYFrame, paintAxis)

            canvas.drawCircle(startX, startY, 10F, paintPoint)
            canvas.drawCircle(endX, endY, 10F, paintPoint)
        }
    }

    private fun drawLinePrimary(canvas: Canvas) {
        (1..6).forEach { numericalOther ->
            try {
                val valueStart = (values[numericalOther]!! * radius)
                val valueEnd = (values[numericalOther + 1]!! * radius)
                drawLinePrimary(canvas, numericalOther, valueStart, valueEnd)
            } catch (ex: Exception) {
                val valueStart = (values[numericalOther]!! * radius)
                val valueEnd = (values[1]!! * radius)
                drawLinePrimary(canvas, numericalOther, valueStart, valueEnd)
            }
        }
    }

    private fun drawLinePrimary(
        canvas: Canvas, numericalOther: Int, valueStart: Float, valueEnd: Float
    ) {
        val corner = 60.0
        val paintAxis = Paint().apply {
            color = Color.RED
            strokeWidth = 10F
        }

        val startX: Float =
            (cos(Math.toRadians(((numericalOther) * corner) + 30)).toFloat() * valueStart) + xCenter
        val startY: Float =
            (sin(Math.toRadians(((numericalOther) * corner) + 30)).toFloat() * valueStart) + yCenter

        val endX: Float =
            cos(Math.toRadians(((numericalOther + 1) * corner) + 30)).toFloat() * valueEnd + xCenter
        val endY: Float =
            sin(Math.toRadians(((numericalOther + 1) * corner) + 30)).toFloat() * valueEnd + yCenter

        canvas.drawLine(startX, startY, endX, endY, paintAxis)

        canvas.drawCircle(startX, startY, 8F, paintPoint)
        canvas.drawCircle(endX, endY, 8F, paintPoint)
    }
}