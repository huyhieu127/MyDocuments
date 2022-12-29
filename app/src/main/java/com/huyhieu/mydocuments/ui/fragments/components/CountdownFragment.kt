package com.huyhieu.mydocuments.ui.fragments.components

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.color
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCountdownBinding
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt


class CountdownFragment : BaseFragment<FragmentCountdownBinding>() {

    override fun FragmentCountdownBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        val timerCountdown = 120
        lifecycleScope.launchWhenResumed {
            repeat(timerCountdown) {
                val secondDisplay = abs(it - timerCountdown)
                val process = ((secondDisplay / timerCountdown.toFloat()) * 100)
                logDebug("Timer: $secondDisplay - Progress: ${process.roundToInt()}")
                tvTimer.text = secondDisplay.toString()
                // prgCountdown.progress = (process.roundToInt())
                prgCountdown.setProgressCompat(process.roundToInt(), true)
                if (secondDisplay == 90) {
                    prgCountdown.setIndicatorColor(Color.RED)
                    tvTimer.setTextColor(Color.RED)
                    tvTextSecond.setTextColor(Color.RED)
                }
                if (secondDisplay == 60) {
                    prgCountdown.setIndicatorColor(Color.YELLOW)
                    tvTimer.setTextColor(Color.YELLOW)
                    tvTextSecond.setTextColor(Color.YELLOW)
                }
                if (secondDisplay == 30) {
                    prgCountdown.setIndicatorColor(Color.GREEN)
                    tvTimer.setTextColor(Color.GREEN)
                    tvTextSecond.setTextColor(Color.GREEN)
                }
                delay(1000L)
            }
            prgCountdown.setProgressCompat(0, true)
            tvTimer.text = "0"
        }
        val timerCountdown2 = 120
        lifecycleScope.launchWhenResumed {
            repeat(timerCountdown) {
                val secondDisplay = abs(it - timerCountdown2)
                tvTimer2.text = secondDisplay.toString()
                // prgCountdown.progress = (process.roundToInt())
                if (secondDisplay == 90) {
                    //prgMyCountdown.setIndicatorColor(Color.RED)
                    tvTimer2.setTextColor(Color.RED)
                    tvTextSecond2.setTextColor(Color.RED)
                }
                if (secondDisplay == 60) {
                    //prgMyCountdown.setIndicatorColor(Color.YELLOW)
                    tvTimer2.setTextColor(Color.YELLOW)
                    tvTextSecond2.setTextColor(Color.YELLOW)
                }
                if (secondDisplay == 30) {
                    //prgMyCountdown.setIndicatorColor(Color.GREEN)
                    tvTimer2.setTextColor(Color.GREEN)
                    tvTextSecond2.setTextColor(Color.GREEN)
                }
                delay(1000L)
            }
            tvTimer.text = "0"
            tvTimer2.setTextColor(context.color(com.huyhieu.library.R.color.colorPrimary))
        }
    }
}