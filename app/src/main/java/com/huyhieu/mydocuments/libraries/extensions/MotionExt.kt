package com.huyhieu.mydocuments.libraries.extensions

import androidx.constraintlayout.motion.widget.MotionLayout


fun MotionLayout.setTransitionTo(startID: Int, endID: Int, duration: Int = 300, onComplete: Runnable? = null) {
    post {
        setTransition(startID, endID)
        setTransitionDuration(duration)
        transitionToEnd(onComplete)
    }
}

fun MotionLayout.onTransitionCompleted(onComplete: ((motionLayout: MotionLayout?, currentId: Int) -> Unit)? = null) {
    addTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int
        ) {

        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            onComplete?.invoke(motionLayout, currentId)
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    })
}