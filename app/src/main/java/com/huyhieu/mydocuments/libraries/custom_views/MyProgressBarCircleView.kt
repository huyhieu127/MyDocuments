package com.huyhieu.mydocuments.libraries.custom_views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.dimenPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class MyProgressBarCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var maxProgressValue: Float = 120F
    var countdown: ((second: Int) -> Unit)? = null
    var duration: Long = 100L

    private var valueCurrent = 0F
    private var thickness = 0

    private var paintPrimary: Paint? = null
    private var paintSecondary: Paint? = null

    private val rect by lazy {
        val space = thickness.toFloat()
        RectF(0F + space, 0F + space, (width - space), (height - space))
    }

    //Radius start - end point
    private val centerX by lazy { width / 2F }
    private val centerY by lazy { height / 2F }
    private val radius by lazy {
        if (width > height) {
            (height / 2F) - thickness
        } else {
            (width / 2F) - thickness
        }
    }
    private var paintStartPoint: Paint? = null
    private var paintEndPoint: Paint? = null

    init {
        thickness = context.dimenPx(com.intuit.sdp.R.dimen._12sdp)

        paintPrimary = Paint()
        paintPrimary?.apply {
            isDither = true
            style = Paint.Style.STROKE
            color = context.color(R.color.colorPrimaryMiddle)
            strokeWidth = thickness.toFloat()
            isAntiAlias = true
        }

        paintSecondary = Paint()
        paintSecondary?.apply {
            isDither = true
            style = Paint.Style.STROKE
            color = context.color(R.color.colorPrimaryLight)
            strokeWidth = thickness.toFloat()
            isAntiAlias = true
        }

        paintStartPoint = Paint()
        paintStartPoint?.apply {
            isDither = true
            style = Paint.Style.FILL
            color = context.color(R.color.colorPrimaryMiddle)
            isAntiAlias = true
        }

        paintEndPoint = Paint()
        paintEndPoint?.apply {
            isDither = true
            style = Paint.Style.FILL
            color = context.color(R.color.colorPrimary)
            isAntiAlias = true
            setShadowLayer(5F, 0F, 4F, context.color(R.color.black_10))
        }

        runAnimation()
        countdownTimer()
    }

    fun setThickness(thickness: Int) {
        this.thickness = thickness
        this.paintPrimary?.strokeWidth = thickness.toFloat()
        this.paintSecondary?.strokeWidth = thickness.toFloat()
        invalidate()
    }

    fun setColorPrimary(@ColorInt color: Int) {
        this.paintPrimary?.color = color
        this.paintStartPoint?.color = color
        invalidate()
    }

    fun setColorSecondary(@ColorInt color: Int) {
        this.paintSecondary?.color = color
        invalidate()
    }

    fun setColorPointEnd(@ColorInt color: Int) {
        this.paintEndPoint?.color = color
        invalidate()
    }

    private fun countdownTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(maxProgressValue.toInt()) {
                val secondDisplay = abs(it - maxProgressValue.toInt())
                countdown?.invoke(secondDisplay)
                delay(duration)
            }
            countdown?.invoke(0)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val angle = -(360 * (valueCurrent / maxProgressValue))
        canvas.drawArc(rect, 270F, 360F, false, paintSecondary!!)
        canvas.drawArc(rect, 270F, angle, false, paintPrimary!!)

        val startX: Float = cos(Math.toRadians(270.0)).toFloat() * radius + centerX
        val startY: Float = sin(Math.toRadians(270.0)).toFloat() * radius + centerY
        val endX: Float = cos(Math.toRadians((270.0 + angle))).toFloat() * radius + centerX
        val endY: Float = sin(Math.toRadians((270.0 + angle))).toFloat() * radius + centerY

        canvas.drawCircle(startX, startY, thickness / 2F, paintStartPoint!!);
        canvas.drawCircle(endX, endY, thickness / 1.25F, paintEndPoint!!)
    }

    private fun runAnimation() {
        val animator = ValueAnimator.ofFloat(maxProgressValue, 0F)
        animator.duration = maxProgressValue.toLong() * duration
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            valueCurrent = value
            invalidate()
        }
        animator.start()
    }
}