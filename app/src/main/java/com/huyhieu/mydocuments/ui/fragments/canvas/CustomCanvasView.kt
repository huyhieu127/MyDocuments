package com.huyhieu.mydocuments.ui.fragments.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.huyhieu.mydocuments.R
import kotlin.math.abs


class CustomCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var axisX = mutableMapOf<Float, Float>()
    private var axisY = mutableMapOf<Float, Float>()
    val points = mutableListOf(
        Pair(1, 1),
        Pair(2, 7),
        Pair(5, 3),
        Pair(7, 5),
        Pair(8, 9),
        Pair(10, 10),
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        //drawRotateImage(canvas!!)
        //drawCircle(canvas)
        drawLine(canvas)
        //drawColumn(canvas)
        drawChart(canvas, points)
    }

    private fun drawChart(canvas: Canvas, points: MutableList<Pair<Int, Int>>) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5F
            color = Color.RED
            style = Paint.Style.FILL_AND_STROKE
        }
        points.forEachIndexed { index, pair ->
            if (index == 0) {
                canvas.drawLine(
                    lAxisY, //x0
                    bAxisX + heightChart, //y0
                    axisX[pair.first.toFloat()] ?: 0F,
                    axisY[pair.second.toFloat()] ?: 0F,
                    paint
                )
            } else {
                val itemOld = points[index - 1]
                canvas.drawLine(
                    axisX[itemOld.first.toFloat()] ?: 0F,
                    axisY[itemOld.second.toFloat()] ?: 0F,
                    axisX[pair.first.toFloat()] ?: 0F,
                    axisY[pair.second.toFloat()] ?: 0F,
                    paint
                )
            }
        }
    }

    private fun drawRotateImage(canvas: Canvas) {
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
        for (i in 1..sizeXChart) {
            val point = (i * wX) + lAxisY
            val rect = Rect()
            paintText.getTextBounds("$i", 0, "$i".length, rect)
            canvas.drawText("$i", (point + (rect.width() / 2)), bNumAxisX + tAxisX, paintText)
            canvas.drawLine(point, tAxisX, point, heightChart + tAxisX, paintMinor)
            axisX[i.toFloat()] = point
        }
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
    }
}