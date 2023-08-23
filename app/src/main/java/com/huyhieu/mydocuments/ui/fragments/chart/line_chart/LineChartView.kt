package com.huyhieu.mydocuments.ui.fragments.chart.line_chart

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.toRectF
import com.huyhieu.mydocuments.R
import kotlin.math.abs


class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var axisX = mutableMapOf<Float, Float>()
    private var axisY = mutableMapOf<Float, Float>()
    private val points = mutableListOf(
        Pair(1F, 1F),
        Pair(2F, 7F),
        Pair(5F, 3F),
        Pair(7F, 5F),
        Pair(8F, 9F),
        Pair(10F, 10F),
    )
    private val pointsValues = mutableListOf<Pair<Float, Float>>()

    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5F
        color = Color.RED
        style = Paint.Style.STROKE
        alpha = 220
    }
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5F
        //color = Color.RED
        style = Paint.Style.FILL
        alpha = 220
    }
    private val rect = Rect(
        x(0F).toInt(),
        y(0F).toInt(),
        x(0F).toInt(),
        y(0F).toInt(),
    )
    private val colors = mutableListOf(Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GREEN)

    private var isDrawLine = false
    private fun runAnimation() {
        val propertyValuesHolders = mutableListOf<PropertyValuesHolder>()
        points.forEach {
            propertyValuesHolders.add(PropertyValuesHolder.ofFloat(it.first.toString(), heightChart + tAxisX, y(it.second)))
        }
        ValueAnimator.ofPropertyValuesHolder(*propertyValuesHolders.toTypedArray()).apply {
            addUpdateListener { valueAnimator ->
                pointsValues.clear()
                points.forEach {
                    val value = valueAnimator.getAnimatedValue("${it.first}") as Float
                    pointsValues.add(Pair(it.first, value))
                    //logDebug("drawChart: $pointsValues")
                }
                invalidate()
            }
            interpolator = DecelerateInterpolator(1F)
            duration = 500
            start()
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
        if (points.isNotEmpty()) {
            drawChart(canvas, points, pointsValues)
        }
    }

    private fun x(axis: Float) = axisX[axis] ?: 0F
    private fun y(axis: Float, isAnimated: Boolean = false) = if (isAnimated) axis else axisY[axis] ?: 0F

    private fun drawChart(canvas: Canvas, pointsRound: MutableList<Pair<Float, Float>>, pointsAnimated: MutableList<Pair<Float, Float>>) {
        val isAnimated = pointsAnimated.isNotEmpty()
        val points = pointsAnimated.ifEmpty { pointsRound }
        points.forEachIndexed { index, pair ->
            //val idx = floor(Math.random() * (colors.size)).roundToInt()
            val color = colors[0]
            paintFill.shader = LinearGradient(
                0F,
                0F,
                50F,
                height.toFloat(),
                color,
                Color.TRANSPARENT,
                Shader.TileMode.MIRROR
            )
            paintLine.color = color
            if (index == 0) {
                canvas.drawLine(
                    x(0F),
                    y(0F),
                    x(pair.first),
                    y(pair.second, isAnimated),
                    paintLine
                )
                val path = Path().apply {
                    this.moveTo(x(0F), y(0F))
                    this.lineTo(x(pair.first), y(0F))
                    this.lineTo(x(pair.first), y(pair.second, isAnimated))
                    this.fillType = Path.FillType.EVEN_ODD
                }
                path.addRect(rect.toRectF(), Path.Direction.CW)
                canvas.drawPath(path, paintFill)
            } else {
                val itemOld = points[index - 1]
                canvas.drawLine(
                    x(itemOld.first),
                    y(itemOld.second, isAnimated),
                    x(pair.first),
                    y(pair.second, isAnimated),
                    paintLine
                )
                val path = Path().apply {
                    this.moveTo(x(itemOld.first), y(itemOld.second, isAnimated))
                    this.lineTo(x(itemOld.first), y(0F))
                    this.lineTo(x(pair.first), y(0F))
                    this.lineTo(x(pair.first), y(pair.second, isAnimated))
                    this.fillType = Path.FillType.EVEN_ODD
                }
                path.addRect(rect.toRectF(), Path.Direction.CW)
                canvas.drawPath(path, paintFill)
            }
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

    /*private fun drawRotateImage(canvas: Canvas) {
        var bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.img_pj
        )
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 402, 528, false)
        val matrix = Matrix()
        matrix.postRotate(90F)
        matrix.postTranslate(528F, 402F)
        canvas.drawBitmap(bitmap, matrix, null)
    }

    private fun drawCirle(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL
        canvas.drawCircle(50f, 60f, 50f, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(200f, 60f, 50f, paint)
    }

    private fun drawColumn(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        //paint.style = Paint.Style.STROKE
        val rectV = Rect()
        rectV.top = 10
        rectV.bottom = measuredHeight - 10

        canvas.drawRect(rectV, paint)
        val rectH = Rect()
        rectH.bottom = measuredHeight - 10
        rectH.right = measuredWidth
        canvas.drawRect(rectH, paint)
    }*/
}