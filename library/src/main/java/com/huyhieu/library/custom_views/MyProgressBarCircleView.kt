package com.huyhieu.library.custom_views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.huyhieu.library.R
import com.huyhieu.library.extensions.color
import com.huyhieu.library.extensions.dimenPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


class MyProgressBarCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var maxProgressValue: Float = 120F
    var countdown: ((second: Int) -> Unit)? = null

    private var valueCurrent = 0F
    private var thickness = 0

    private var paintPrimary: Paint? = null
    private var paintSecondary: Paint? = null

    private val rect by lazy {
        val space = thickness / 2F
        RectF(0F + space, 0F + space, (width - space), (height - space))
    }


    init {
        thickness = context.dimenPx(com.intuit.sdp.R.dimen._12sdp)

        paintPrimary = Paint()
        paintPrimary?.apply {
            isDither = true
            style = Paint.Style.STROKE
            color = context.color(R.color.blue)
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
        invalidate()
    }

    fun setColorSecondary(@ColorInt color: Int) {
        this.paintSecondary?.color = color
        invalidate()
    }

    private fun countdownTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(maxProgressValue.toInt()) {
                val secondDisplay = abs(it - maxProgressValue.toInt())
                countdown?.invoke(secondDisplay)
                delay(1000L)
            }
            countdown?.invoke(0)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rect, 270f, 360f, false, paintSecondary!!)
        canvas.drawArc(rect, 270f, -(360 * (valueCurrent / maxProgressValue)), false, paintPrimary!!)
    }

    private fun runAnimation() {
        val animator = ValueAnimator.ofFloat(maxProgressValue, 0F)
        animator.duration = maxProgressValue.toLong() * 1000L
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            valueCurrent = value
            invalidate()
        }
        animator.start()
    }
}