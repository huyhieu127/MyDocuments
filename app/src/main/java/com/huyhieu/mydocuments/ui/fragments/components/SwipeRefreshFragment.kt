package com.huyhieu.mydocuments.ui.fragments.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSwipeRefreshBinding
import com.huyhieu.mydocuments.libraries.extensions.onTransitionCompleted
import com.huyhieu.mydocuments.libraries.extensions.showToastShort
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SwipeRefreshFragment : BaseFragment<FragmentSwipeRefreshBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        handleOnTouchView(nstView)
        handleOnTouchView(rcv)
        lifecycleScope.launch {
            delay(2000L)
        }
        root.onTransitionCompleted { _, currentId ->
            when (currentId) {
                R.id.refresh -> {
                    context.showToastShort("Loading...")
                    lifecycleScope.launch {
                        delay(1000)
                        if (root.progress != 0F && root.currentState == R.id.refresh) {
                            root.transitionToStart()
                        }
                        context.showToastShort("Done!")
                    }
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun FragmentSwipeRefreshBinding.handleOnTouchView(view: View) {
        var yView = 0F
        view.setOnTouchListener { _, event ->
            if (root.progress == 0f && root.startState == R.id.start) {
                /*if (yView >= event.y && !usuperHistoryAdapter.dataIsEmpty) {
                    root.setTransition(R.id.transAnchor)
                } else {
                    root.setTransition(R.id.transRefresh)
                }
                recyclerViewUsuperHistory.scrollToPosition(0)*/
                if (yView >= event.y) {
                    root.setTransition(R.id.transAnchor)
                } else {
                    root.setTransition(R.id.transRefresh)
                }
                rcv.scrollToPosition(0)
                logDebug("$yView - ${event.y}")
            }
            yView = event.y
            return@setOnTouchListener false
        }
    }
}