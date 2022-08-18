package com.huyhieu.mydocuments.utils.commons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.huyhieu.mydocuments.App
import com.huyhieu.mydocuments.R

class HoleCircle(
    var view: View?,
    var padding: Float = App.getInstance().resources.getDimension(R.dimen.hole_padding)
)

class HoleRectangle(
    var view: View?,
    var radius: Float = 0F,
    var padding: Float = App.getInstance().resources.getDimension(R.dimen.hole_padding)
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
    }

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