package com.huyhieu.mydocuments.libraries.commons.bubblechat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.huyhieu.mydocuments.databinding.WidgetBubbleChatBinding


@SuppressLint("ViewConstructor", "ClickableViewAccessibility")
class BubbleView @JvmOverloads constructor(
    private val mWindowManager: WindowManager,
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr), View.OnTouchListener {

    private val vb = WidgetBubbleChatBinding.inflate(LayoutInflater.from(mContext), null, false)

    private var params: WindowManager.LayoutParams
    private var screenWith: Int
    private var screenHeight: Int

    var initialX = 0
    var initialY = 0
    var initialTouchX = 0f
    var initialTouchY = 0f


    init {
        //Create Layout param
        params = setWindowManagerLayoutParams()
        mWindowManager.addView(vb.root, params)
        //implement touch listener
        //Get screen width
        val display = mWindowManager.defaultDisplay
        screenWith = display.width
        screenHeight = display.height
        vb.root.setOnTouchListener(this)
    }


    private fun setWindowManagerLayoutParams(): WindowManager.LayoutParams {
        val lp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        //Specify position bubble
        lp.gravity = Gravity.TOP or Gravity.START
        //Initially view will be added to top-left corner
        lp.x = 0
        lp.y = 0
        return lp
    }

    fun removeBubbleView() {
        //Remove chat head
        mWindowManager.removeView(vb.root)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event ?: return false
        v ?: return false
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //save start position
                initialX = params.x
                initialY = params.y

                initialTouchX = event.rawX
                initialTouchY = event.rawY
                true
            }

            MotionEvent.ACTION_UP -> {
//                if (params.x < screenWith / 2) {
//                    while (params.x > 0) {
//                        params.x -= 1
//                        mWindowManager.updateViewLayout(vb.root, params)
//                        /*mBubbleView.animate().translationX(params.x.toFloat()).setDuration(500)
//                            .start()*/
//                    }
//                } else {
//                    while (params.x < screenWith) {
//                        params.x += 1
//                        mWindowManager.updateViewLayout(vb.root, params)
//                    }
//                }
                true
            }

            MotionEvent.ACTION_MOVE -> {
                /*params.x = event.rawX.toInt() - vb.root.width / 2
                params.y = (event.rawY - vb.root.height / 2 - App.heightStatusBar).toInt()*/
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                mWindowManager.updateViewLayout(vb.root, params)
                true
            }

            else -> false
        }
    }
//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        event ?: return false
//        v ?: return false
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                //save start position
//                initialX = params.x
//                initialY = params.y
//
//                initialTouchX = event.rawX
//                initialTouchY = event.rawY
//                true
//            }
//            MotionEvent.ACTION_UP -> {
//                if (params.x < screenWith / 2) {
//                    while (params.x > 0) {
//                        params.x -= 1
//                        mWindowManager.updateViewLayout(vb.root, params)
//                        /*mBubbleView.animate().translationX(params.x.toFloat()).setDuration(500)
//                            .start()*/
//                    }
//                } else {
//                    while (params.x < screenWith) {
//                        params.x += 1
//                        mWindowManager.updateViewLayout(vb.root, params)
//                    }
//                }
//                true
//            }
//            MotionEvent.ACTION_MOVE -> {
//                params.x = initialX + (event.rawX - initialTouchX).toInt()
//                params.y = initialY + (event.rawY - initialTouchY).toInt()
//                mWindowManager.updateViewLayout(vb.root, params)
//                true
//            }
//        }
//        return false
//    }
}