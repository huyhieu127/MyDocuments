package com.huyhieu.mydocuments.utils.extensions

import androidx.constraintlayout.motion.widget.MotionLayout


fun MotionLayout.setTransitionTo(startID: Int, endID: Int, duration: Int = 500) {
    post {
        setTransition(startID, endID)
        setTransitionDuration(duration)
        transitionToEnd()
    }
}