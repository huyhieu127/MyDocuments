package com.huyhieu.mydocuments.ui.fragments.widget

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCountdownBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt


class CountdownFragment : BaseFragment<FragmentCountdownBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?): Unit = with(vb) {
        mActivity.setDarkColorStatusBar()
        val timerCountdown = 120
        lifecycleScope.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
            tvTimer.setTextColor(context.color(R.color.colorPrimaryLight))
            tvTextSecond.setTextColor(context.color(R.color.colorPrimaryLight))
        }
        lifecycleScope.launchWhenResumed {
            prgMyCountdown.countdown = { secondDisplay ->
                tvTimer2.text = secondDisplay.toString()
                // prgCountdown.progress = (process.roundToInt())
                if (secondDisplay == 90) {
                    tvTimer2.setTextColor(context.color(R.color.colorRedAlert))
                    tvTextSecond2.setTextColor(context.color(R.color.colorRedAlert))

                    prgMyCountdown.setColorPointEnd(context.color(R.color.colorRedAlert))
                    prgMyCountdown.setColorPrimary(context.color(R.color.colorRedAlertMiddle))
                    prgMyCountdown.setColorSecondary(context.color(R.color.colorRedAlertLight))
                }
                if (secondDisplay == 60) {
                    tvTimer2.setTextColor(context.color(R.color.colorSecondary))
                    tvTextSecond2.setTextColor(context.color(R.color.colorSecondary))

                    prgMyCountdown.setColorPointEnd(context.color(R.color.colorSecondary))
                    prgMyCountdown.setColorPrimary(context.color(R.color.colorSecondaryMiddle))
                    prgMyCountdown.setColorSecondary(context.color(R.color.colorSecondaryLight))
                }
                if (secondDisplay == 30) {
                    tvTimer2.setTextColor(context.color(R.color.colorGreenMint))
                    tvTextSecond2.setTextColor(context.color(R.color.colorGreenMint))

                    prgMyCountdown.setColorPointEnd(context.color(R.color.colorGreenMint))
                    prgMyCountdown.setColorPrimary(context.color(R.color.colorGreenMiddle))
                    prgMyCountdown.setColorSecondary(context.color(R.color.colorGreenLight))
                }
                if (secondDisplay == 0) {
                    tvTimer2.text = "0"
                    /*tvTimer2.setTextColor(context.color(R.color.colorGreenLight))
                    tvTextSecond2.setTextColor(context.color(R.color.colorGreenLight))
                    prgMyCountdown.setColorPointEnd(context.color(R.color.colorGreenLight))*/
                }
            }
        }
    }
}