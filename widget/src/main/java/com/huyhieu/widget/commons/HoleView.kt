package com.huyhieu.widget.commons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.huyhieu.widget.R

class HoleCircle(
    var view: View?,
    var padding: Float = view?.context?.resources?.getDimension(R.dimen.hole_padding) ?: 0F
)

class HoleRectangle(
    var view: View?,
    var radius: Float = 0F,
    var padding: Float = view?.context?.resources?.getDimension(R.dimen.hole_padding) ?: 0F
)

class HoleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint()
    private var holePaint: Paint = Paint()
    private var bitmap: Bitmap? = null
    private var layer: Canvas? = null

    //position of hole
    var holeCircle: HoleCircle? = null
        set(value) {
            field = value
            //redraw
            this.invalidate()
        }
    var holeRectangle: HoleRectangle? = null
        set(value) {
            field = value
            //redraw
            this.invalidate()
        }

    var sizeImage: Point? = null
    var rectResult: Rect? = null
        set(value) {
            field = value
            //redraw
            this.invalidate()
        }

    var pointsResult: Array<Point>? = null
        set(value) {
            field = value
            //redraw
            this.invalidate()
        }


    private val paintResult by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 6F
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap == null) {
            configureBitmap()
        }

        //draw background
        layer?.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)

        //draw hole
        holeCircle?.let { holeCircle ->
            holeCircle.view?.let { view ->
                val radius = if (view.height > view.width) {
                    view.height / 2
                } else {
                    view.width / 2
                }
                val vRadius = radius + holeCircle.padding
                val vX = view.x + (view.width / 2)
                val vY = view.y + (view.height / 2)
                layer?.drawCircle(vX, vY, vRadius, holePaint)
            }
        }
        holeRectangle?.let { holeRectangle ->
            holeRectangle.view?.let { view ->
                val vLeft = view.left.toFloat() - holeRectangle.padding
                val vTop = view.top.toFloat() - holeRectangle.padding
                val vRight = view.right.toFloat() + holeRectangle.padding
                val vBottom = view.bottom.toFloat() + holeRectangle.padding
                val rectF = RectF(vLeft, vTop, vRight, vBottom)

                layer?.drawRoundRect(
                    rectF,
                    holeRectangle.radius,
                    holeRectangle.radius,
                    holePaint
                )
            }
        }

        //draw bitmap
        canvas.drawBitmap(bitmap!!, 0.0f, 0.0f, paint)
        rectResult?.let {
            sizeImage ?: return
            val rect = Rect()
            rect.left = x(it.left).toInt()
            rect.right = x(it.right).toInt()

            rect.top = y(it.top).toInt()
            rect.bottom = y(it.bottom).toInt()
            canvas.drawRect(it, paintResult)
        }

        pointsResult?.let {
            sizeImage ?: return
            it.forEachIndexed { idx, point ->
                if (idx > 0) {
                    val pointOld = it[idx - 1]
                    canvas.drawLine(
                        x(pointOld),
                        y(pointOld),
                        x(it[idx]),
                        y(it[idx]),
                        paintResult
                    )
                } else {
                    val pointOld = it[it.size - 1]
                    canvas.drawLine(
                        x(pointOld),
                        y(pointOld),
                        x(it[0]),
                        y(it[0]),
                        paintResult
                    )
                }
            }
        }
    }

    private fun x(point: Point) = x(point.x)

    private fun y(point: Point) = y(point.y)

    private fun x(axis: Int) = (axis.toFloat() * width.toFloat()) / (sizeImage?.x?.toFloat() ?: 0F)

    private fun y(axis: Int) = (axis.toFloat() * height.toFloat()) / (sizeImage?.y?.toFloat() ?: 0F)

    private fun configureBitmap() {
        //create bitmap and layer
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        layer = Canvas(bitmap!!)
    }

    init {
        //configure background color
        val backgroundAlpha = 0.8
        paint.color = ColorUtils.setAlphaComponent(
            ContextCompat.getColor(this.context, R.color.black), (255 * backgroundAlpha).toInt()
        )

        //configure hole color & mode
        holePaint.color = ContextCompat.getColor(this.context, android.R.color.transparent)
        holePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}