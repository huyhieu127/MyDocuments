package com.huyhieu.mydocuments.utils.extensions

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager

fun ConstraintLayout.editConstraint(block: ConstraintSet.() -> Unit) {
    ConstraintSet().also {
        it.clone(this)
        it.block()
        it.applyTo(this)
    }
}

fun ConstraintLayout.beginTransition(
    transition: Transition,
    block: ConstraintSet.() -> Unit
): Transition {
    val set = ConstraintSet().also {
        it.clone(this)
        it.block()
    }
    TransitionManager.beginDelayedTransition(this, transition)
    set.applyTo(this)
    return transition
}

fun ConstraintLayout.beginTransition(
    transition: Transition,
    block: ConstraintSet.() -> Unit,
    onEnd: () -> Unit
): Transition {
    transition.addListener(object : SimpleTransitionListener {
        override fun onTransitionEnd(transition: Transition) {
            transition.removeListener(this)
            onEnd()
        }
    })
    return beginTransition(transition, block)
}

fun ConstraintLayout.beginTransition(duration: Long, block: ConstraintSet.() -> Unit) {
    val transition = ChangeBounds().also {
        it.duration = duration
    }
    beginTransition(transition, block)
}

fun ConstraintLayout.beginTransition(
    duration: Long,
    block: ConstraintSet.() -> Unit,
    onEnd: () -> Unit
) {
    val transition = ChangeBounds().also {
        it.duration = duration
    }
    beginTransition(transition, block, onEnd)
}

fun MotionLayout.transitionToState(transitionId: Int, onCompleted: () -> Unit) {
    addTransitionListener(object : SimpleMotionTransitionListener {
        override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
            removeTransitionListener(this)
            onCompleted()
        }
    })
    transitionToState(transitionId)

}

class ConstraintBuilder(private val constraintLayout: ConstraintLayout) {

    private val constraintSetList = mutableListOf<ConstraintSet.() -> Unit>()

    private val transitionList = mutableListOf<Transition>()

    fun transform(_duration: Long = 400, block: ConstraintSet.() -> Unit): ConstraintBuilder {
        transitionList.add(ChangeBounds().also {
            it.duration = _duration
        })
        constraintSetList.add(block)
        return this
    }

    fun start() {
        if (transitionList.isEmpty()) return
        for (i in 0..transitionList.lastIndex) {
            transitionList[i].addListener(object : SimpleTransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    transitionList[i].removeListener(this)
                    if (i < transitionList.lastIndex) {
                        constraintLayout.beginTransition(
                            transitionList[i + 1],
                            constraintSetList[i + 1]
                        )
                    }
                }
            })
        }
        constraintLayout.beginTransition(transitionList[0], constraintSetList[0])
    }

}

open class ItemTransitionListener(private val itemView: View, private val position: Int) :
    SimpleMotionTransitionListener {

    override fun equals(other: Any?): Boolean {
        return position == (other as? ItemTransitionListener)?.position
    }

    override fun onTransitionStarted(layout: MotionLayout, startId: Int, endId: Int) {
        itemView.parent.requestDisallowInterceptTouchEvent(true)
    }

    override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
        itemView.parent.requestDisallowInterceptTouchEvent(false)
    }
}

interface SimpleMotionTransitionListener : MotionLayout.TransitionListener {
    override fun onTransitionChange(
        layout: MotionLayout,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
    }

    override fun onTransitionStarted(layout: MotionLayout, startId: Int, endId: Int) {
    }

    override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
    }

    override fun onTransitionTrigger(
        layout: MotionLayout,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }
}

interface SimpleTransitionListener : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {
    }

    override fun onTransitionEnd(transition: Transition) {
    }

    override fun onTransitionCancel(transition: Transition) {
    }

    override fun onTransitionPause(transition: Transition) {
    }

    override fun onTransitionResume(transition: Transition) {
    }

}