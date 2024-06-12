package com.huyhieu.mydocuments.ui.fragments.chart.radar_chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Path.FillType
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.huyhieu.data.logger.logDebug
import com.huyhieu.data.logger.logError
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.extensions.color
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

    private val numTop = 6
    private val corner get() = 360.0 / numTop

    private val numberRow = 5
    private val spaceRow get() = radius / numberRow

    private val paintAxis = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 2F
    }

    private val paintDots = Paint().apply {
        isDither = true
        style = Paint.Style.FILL
        color = Color.WHITE
        isAntiAlias = true
        strokeWidth = 10F
        setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFrame(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        //drawFrameCircle(canvas)
        (1..numberRow).forEach {
            drawFrameLine(canvas, it)
        }

        //RED
        val paintBgRed = Paint().apply {
            color = Color.parseColor("#40FF0000")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val paintStrokeRed = Paint().apply {
            color = Color.parseColor("#FF0000")
            strokeWidth = 6F
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        val redValues = mutableMapOf(
            1 to 1.0F, 2 to 0.75F, 3 to 1F, 4 to 0.8F, 5 to 0.8F, 6 to 0.4F
        )
        drawLinePrimary(canvas, redValues, paintBgRed, paintStrokeRed)

        //YELLOW
        val paintBgYellow = Paint().apply {
            color = Color.parseColor("#40FFFF00")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val paintStrokeYellow = Paint().apply {
            color = Color.parseColor("#FFFF00")
            strokeWidth = 6F
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        val yellowValues = mutableMapOf(
            1 to 0.8F, 2 to 0.9F, 3 to 0.3F, 4 to 0.9F, 5 to 0.6F, 6 to 0.9F
        )
        drawLinePrimary(canvas, yellowValues, paintBgYellow, paintStrokeYellow)
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
        val radius = spaceRow * numericalOther
        (1..numTop).forEach {
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

            //Dots
            //canvas.drawCircle(startX, startY, 4F, paintDots)

            //Line vertical
            canvas.drawLine(startX, startY, endXFrame, endYFrame, paintAxis)
            //Line horizontal
            canvas.drawLine(startX, startY, endX, endY, paintAxis)
        }
    }

    private fun drawLinePrimary(
        canvas: Canvas, values: MutableMap<Int, Float>, paintBg: Paint, paintStroke: Paint
    ) {
        val mapPoints = mutableMapOf<Int, Float>()
        (1..numTop).forEach { numericalOther ->
            try {
                val valueStart = values.valueStart(numericalOther)
                val valueEnd = values.valueEnd(numericalOther)
                //drawLinePrimary(canvas, values, numericalOther, valueStart, valueEnd, paint)
                mapPoints[numericalOther] = valueStart
            } catch (ex: Exception) {
                val valueStart = values.valueStart(numericalOther)
                val valueEnd = values.valueEnd(0)//First value
                //drawLinePrimary(canvas, values, numericalOther, valueStart, valueEnd, paint)
                mapPoints[numericalOther] = valueStart
            }
        }

        val path = Path().also {
            it.fillType = FillType.EVEN_ODD
        }.apply {
            for ((key, value) in mapPoints) {
                val x = value.measureAxisX(key)
                val y = value.measureAxisY(key)
                if (key == 1) {
                    moveTo(x, y)
                } else {
                    lineTo(x, y)
                }
            }
            close()
        }
        //Draw background
        canvas.drawPath(path, paintBg)
        //Draw stroke
        canvas.drawPath(path, paintStroke)
    }

    private fun drawLinePrimary(
        canvas: Canvas,
        values: MutableMap<Int, Float>,
        numericalOther: Int,
        valueStart: Float,
        valueEnd: Float,
        paint: Paint
    ) {
        val startX: Float = valueStart.measureAxisX(numericalOther)
        val startY: Float = valueStart.measureAxisY(numericalOther)
        val endX: Float = valueEnd.measureAxisX(numericalOther + 1)
        val endY: Float = valueEnd.measureAxisY(numericalOther + 1)

        canvas.drawLine(startX, startY, endX, endY, paint)

        canvas.drawCircle(startX, startY, 8F, paintDots)
        canvas.drawCircle(endX, endY, 8F, paintDots)

        val path = Path().apply {
            fillType = FillType.EVEN_ODD
            moveTo(startX, startY)
            lineTo(endX, endY)
            lineTo(xCenter, yCenter)
            close()
        }
        logDebug("$startX - $startY --- $endX - $endY")
        canvas.drawPath(path, paint)
    }

    private fun Float.measureAxisX(numericalOther: Int): Float {
        return (cos(Math.toRadians(((numericalOther) * corner) + 30)).toFloat() * this) + xCenter
    }

    private fun Float.measureAxisY(numericalOther: Int): Float {
        return (sin(Math.toRadians(((numericalOther) * corner) + 30)).toFloat() * this) + yCenter
    }

    private fun MutableMap<Int, Float>.valueStart(numericalOther: Int): Float {
        val start = this[numericalOther] ?: throw Exception("Min length!")
        return when {
            start < 0.0F -> {
                logError("RadarChartView: Value[$start] < 0.0F")
                0.0F
            }
            start > 1.0F -> {
                logError("RadarChartView: Value[$start] > 1.0F")
                1.0F
            }
            else -> start * radius
        }
    }

    private fun MutableMap<Int, Float>.valueEnd(numericalOther: Int): Float {
        val end = this[numericalOther + 1] ?: throw Exception("Max length!")
        return when {
            end < 0.0F -> {
                logError("RadarChartView: Value[$end] < 0.0F")
                0.0F
            }
            end > 1.0F -> {
                logError("RadarChartView: Value[$end] > 1.0F")
                1.0F
            }
            else -> end * radius
        }
    }
}