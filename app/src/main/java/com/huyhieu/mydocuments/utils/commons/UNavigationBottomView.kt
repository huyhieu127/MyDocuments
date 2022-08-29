package com.huyhieu.mydocuments.utils.commons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetUNavigationBottomViewBinding
import com.huyhieu.mydocuments.utils.extensions.showToastShort


enum class UTab {
    TAB_SCAN,
    TAB_HOME,
    TAB_PROFILE
}

class UNavigationBottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val binding =
        WidgetUNavigationBottomViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        with(binding) {
            tabScan.setOnClickListener(this@UNavigationBottomView)
            tabHome.setOnClickListener(this@UNavigationBottomView)
            tabProfile.setOnClickListener(this@UNavigationBottomView)
        }
        setTabSelected(UTab.TAB_HOME)
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v?.id) {
                tabScan.id -> {
                    setTabSelected(UTab.TAB_SCAN) {
                        context.showToastShort("Scan")
                    }
                }
                tabHome.id -> {
                    setTabSelected(UTab.TAB_HOME) {
                        context.showToastShort("Home")
                    }
                }
                tabProfile.id -> {
                    setTabSelected(UTab.TAB_PROFILE) {
                        context.showToastShort("Profile")
                    }
                }
            }
        }
    }

    fun setTabSelected(tab: UTab, onSelected: (() -> Unit)? = null) {
        with(binding) {
            imgScan.isSelected = tab == UTab.TAB_SCAN
            imgHome.isSelected = tab == UTab.TAB_HOME
            imgProfile.isSelected = tab == UTab.TAB_PROFILE

            val lp = tabSelected.layoutParams as ConstraintLayout.LayoutParams
            when (tab) {
                UTab.TAB_SCAN -> {
                    lp.startToStart = R.id.tabScan
                    lp.endToEnd = R.id.tabScan
                }
                UTab.TAB_HOME -> {
                    lp.startToStart = R.id.tabHome
                    lp.endToEnd = R.id.tabHome
                }
                UTab.TAB_PROFILE -> {
                    lp.startToStart = R.id.tabProfile
                    lp.endToEnd = R.id.tabProfile
                }
            }
            tabSelected.layoutParams = lp
        }
        onSelected?.invoke()
    }
}