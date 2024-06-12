package com.huyhieu.mydocuments.ui.fragments.chart.cubic_chart

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.extensions.color
import kotlin.math.abs


class CubicChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val colors =
        mutableListOf(Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GREEN)
    private var axisX = mutableMapOf<Float, Float>()
    private var axisY = mutableMapOf<Float, Float>()
    private val pointsInput = mutableListOf(
        PointF(0F, 0F),
        PointF(1F, 1F),
        PointF(2F, 7F),
        PointF(5F, 3F),
        PointF(7F, 5F),
        PointF(8F, 9F),
        PointF(10F, 10F),
    )
    private val pointsAnimated = mutableListOf<PointF>()

    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5F
        color = colors[3]
        style = Paint.Style.STROKE
        alpha = 220
    }
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5F
        //color = Color.RED
        style = Paint.Style.FILL
        alpha = 220
    }
    private val paintDots = Paint().apply {
        isDither = true
        style = Paint.Style.FILL
        color = colors[1]
        isAntiAlias = true
        setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
    }
    private val paintStrokeDots = Paint().apply {
        isDither = true
        style = Paint.Style.FILL
        color = colors[3]
        isAntiAlias = true
        setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
    }

    private val paintTextAxis = Paint().apply {
        isDither = true
        color = Color.WHITE
        textSize = 24F
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
    }

    private var isDrawLine = false
    private fun runAnimation() {
        val propertyValuesHolders = mutableListOf<PropertyValuesHolder>()
        pointsInput.forEach {
            propertyValuesHolders.add(
                PropertyValuesHolder.ofFloat(
                    it.x.toString(),
                    heightChart + tAxisX,
                    y(it.y)
                )
            )
        }
        ValueAnimator.ofPropertyValuesHolder(*propertyValuesHolders.toTypedArray()).apply {
            addUpdateListener { valueAnimator ->
                pointsAnimated.clear()
                pointsInput.forEach {
                    val value = valueAnimator.getAnimatedValue("${it.x}") as Float
                    pointsAnimated.add(PointF(x(it.x), y(value, true)))
                    //logDebug("drawChart: $pointsValues")
                }
                invalidate()
            }
            interpolator = DecelerateInterpolator(1F)
            duration = 500
            start()
        }
    }

    private val sizeDot = 16F
    private val strokeWidthDot = 6F
    private val dotList by lazy {
        val spaceBound = 20F
        pointsInput.map {
            val x = x(it.x)
            val y = y(it.y)
            RectF(
                /* left = */ x - spaceBound,
                /* top = */y - spaceBound,
                /* right = */x + sizeDot + spaceBound,
                /* bottom = */y + sizeDot + spaceBound
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //drawRotateImage(canvas!!)
        //drawCircle
        drawLine(canvas)
        if (!isDrawLine) {
            isDrawLine = true
            runAnimation()
        }
        //drawColumn(canvas)
        if (pointsInput.isNotEmpty()) {
            drawChart(canvas)
        }
    }

    private fun x(axis: Float) = axisX[axis] ?: 0F
    private fun y(axis: Float, isAnimated: Boolean = false) =
        if (isAnimated) axis else axisY[axis] ?: 0F

    private fun drawChart(canvas: Canvas) {
        val points = pointsAnimated.ifEmpty { pointsInput }
        val path = Path()

        path.moveTo(points[0].x, points[0].y)
        path.cubicCurve(points)
        canvas.drawPath(path, paintLine)

        paintFill.shader = LinearGradient(
            0F,
            0F,
            50F,
            height.toFloat(),
            colors[3],
            Color.TRANSPARENT,
            Shader.TileMode.MIRROR
        )
        path.lineTo(x(pointsInput.last().x), y(0F))
        canvas.drawPath(path, paintFill)

        points.forEachIndexed { idx, it ->
            canvas.drawCircle(it.x, it.y, 16F, paintStrokeDots)
            canvas.drawCircle(it.x, it.y, sizeDot - strokeWidthDot, paintDots)

            val paddingBottom = 10F
            canvas.drawText(
                /* text = */ "(${pointsInput[idx].x}:${pointsInput[idx].y})",
                /* x = */ it.x,
                /* y = */ it.y - (sizeDot + paddingBottom),
                /* paint = */ paintTextAxis
            )
        }
    }

    /**
     * Draw LINE*/
    var widthChart = 0F
    var heightChart = 0F
    private val tAxisX = 100F
    private val bAxisX = 100F
    private val lAxisY = 100F
    private val rAxisY = 100F

    private fun drawLine(canvas: Canvas) {
        val textS = context.resources.getDimension(R.dimen.text_size)
        val paintText = Paint().apply {
            color = Color.WHITE
            textSize = textS
            textAlign = Paint.Align.RIGHT
        }
        val paintAxis = Paint().apply {
            color = Color.WHITE
            strokeWidth = 2F
        }

        heightChart = measuredHeight.toFloat() - bAxisX - tAxisX
        canvas.drawLine(
            0F,
            heightChart + tAxisX,
            measuredWidth.toFloat(),
            heightChart + tAxisX,
            paintAxis
        )

        widthChart = measuredWidth.toFloat() - lAxisY - rAxisY
        canvas.drawLine(lAxisY, 0F, lAxisY, measuredHeight.toFloat(), paintAxis)

        val paintMinor = Paint().apply {
            color = Color.WHITE
            alpha = 100
        }

        val lNumAxisY = lAxisY - 24F
        val sizeYChart = 10
        val hY = heightChart / sizeYChart

        axisY[0F] = bAxisX + heightChart
        for (i in 1..sizeYChart) {
            val point = tAxisX + (abs(i - (sizeYChart + 1)) * hY) - hY
            val rect = Rect()
            paintText.getTextBounds("$i", 0, "$i".length, rect)
            canvas.drawText(
                "$i",
                lNumAxisY,
                point + (rect.height() / 2),
                paintText
            )
            canvas.drawLine(lAxisY, point, measuredWidth.toFloat() - rAxisY, point, paintMinor)
            axisY[i.toFloat()] = point
        }

        val bNumAxisX = heightChart + textS + 24F
        val sizeXChart = 10
        val wX = ((widthChart) / sizeXChart)
        axisX[0F] = lAxisY
        for (i in 1..sizeXChart) {
            val point = (i * wX) + lAxisY
            val rect = Rect()
            paintText.getTextBounds("$i", 0, "$i".length, rect)
            canvas.drawText("$i", (point + (rect.width() / 2)), bNumAxisX + tAxisX, paintText)
            canvas.drawLine(point, tAxisX, point, heightChart + tAxisX, paintMinor)
            axisX[i.toFloat()] = point
        }
    }

    private fun Path.cubicCurve(points: MutableList<PointF>): Path {
        try {
            val conPoint1 = mutableListOf<PointF>()
            val conPoint2 = mutableListOf<PointF>()

            for (i in 1 until points.size) {
                conPoint1.add(PointF((points[i].x + points[i - 1].x) / 2, points[i - 1].y))
                conPoint2.add(PointF((points[i].x + points[i - 1].x) / 2, points[i].y))
            }
            if (points.isEmpty() && conPoint1.isEmpty() && conPoint2.isEmpty()) return this

            for (i in 1 until points.size) {
                this.cubicTo(
                    conPoint1[i - 1].x, conPoint1[i - 1].y, conPoint2[i - 1].x, conPoint2[i - 1].y,
                    points[i].x, points[i].y
                )
            }
        } catch (_: Exception) {
        }
        return this
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x ?: 0F
        val touchY = event?.y ?: 0F
        if (event?.action == MotionEvent.ACTION_DOWN) {
            logDebug("onTouch: touchX($touchX) - touchY($touchY)")
            dotList.forEachIndexed { index, rectF ->
                if (rectF.contains(touchX, touchY)) {
                    try {
                        val pointF =  pointsInput[index]
                        val axis = "(${pointF.x}:${pointF.y})"
                        logDebug("onTouch: axis$axis")
                        chartClickListener?.onDotClicked(pointF)
                    } catch (_: Exception) {
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private var chartClickListener: OnChartClickListener? = null
    fun setOnChartClickListener(chartClickListener: OnChartClickListener) {
        this.chartClickListener = chartClickListener
    }

    interface OnChartClickListener {
        fun onDotClicked(pointF: PointF){}
    }
}

