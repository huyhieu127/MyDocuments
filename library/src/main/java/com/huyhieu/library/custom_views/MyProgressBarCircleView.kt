package com.huyhieu.library.custom_views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.huyhieu.library.R
import com.huyhieu.library.extensions.color
import com.huyhieu.library.extensions.dimenPx


class MyProgressBarCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var valueCurrent = 0F
    var maxProgressValue: Float = 120F

    private val thickness by lazy {
        context.dimenPx(com.intuit.sdp.R.dimen._12sdp)
    }

    private var paintPrimary: Paint? = null
    private var paintSecondary: Paint? = null
    private val rect by lazy {
        val space = thickness / 2F
        RectF(0F + space, 0F + space, (width - space), (height - space))
    }


    init {
        paintPrimary = Paint()
        paintPrimary?.apply {
            isDither = true
            style = Paint.Style.STROKE
            color = context.color(R.color.colorPrimary)
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