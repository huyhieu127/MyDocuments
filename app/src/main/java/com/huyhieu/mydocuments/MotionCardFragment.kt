package com.huyhieu.mydocuments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragment<FragmentMotionCardBinding>() {
    override fun initializeBinding() = FragmentMotionCardBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        mBinding.motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }

    override fun onClick(v: View?) {
    }

    private fun MotionLayout.setTransitionTo(startID: Int, endID: Int, duration: Int = 500) {
        post {
            setTransition(startID, endID)
            //setTransitionDuration(duration)
            transitionToEnd()
        }
    }

}