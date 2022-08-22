package com.huyhieu.mydocuments.ui.introduce

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.navigation.NavOptions
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentIntroduceBinding
import com.huyhieu.mydocuments.utils.directions.MainDirections
import com.huyhieu.mydocuments.utils.extensions.navigate

class IntroduceFragment : BaseFragment<FragmentIntroduceBinding>() {
    private val myViewPagerAdapter by lazy { MyViewPagerAdapter() }
    private var layouts: IntArray? = null

    override fun initializeBinding() = FragmentIntroduceBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        mBinding.apply {
            layouts = intArrayOf(
                R.layout.fragment_introduce_page1,
                R.layout.fragment_introduce_page2,
                R.layout.fragment_introduce_page3
            )

            vpIntroduce.adapter = myViewPagerAdapter
            dotsIndicator.attachTo(vpIntroduce)

            vpIntroduce.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    fillText(position)
                    btnNext.isInvisible = position != layouts!!.size - 1
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            tvSkip.setOnClickListener(this@IntroduceFragment)
            btnNext.setOnClickListener(this@IntroduceFragment)
        }
    }

    private fun fillText(position: Int) {
        with(mBinding) {
            when (position) {
                0 -> {
                    tvName.text = "This is Name"
                    tvTitle.text = "This is Title"
                    tvDescribe.text = "This is Describe This is Describe"
                }
                1 -> {
                    tvName.text = "This is Name 1"
                    tvTitle.text = "This is Title 1"
                    tvDescribe.text =
                        "This is Describe This is Describe This is Describe This is Describe 1"
                }
                2 -> {
                    tvName.text = "This is Name 2"
                    tvTitle.text = "This is Title 2"
                    tvDescribe.text = "This is Describe This is Describe2"
                }
            }
        }
    }

    override fun FragmentIntroduceBinding.onClickViewBinding(v: View) {
        when (v.id) {
            tvSkip.id -> {
                launchHomeScreen()
            }
            btnNext.id -> {
                val current = getCurrentItem()
                if (current < layouts!!.size) {
                    vpIntroduce.currentItem = current
                } else {
                    launchHomeScreen()
                }
            }
        }
    }

    private fun getCurrentItem(): Int = mBinding.vpIntroduce.currentItem + 1

    private fun launchHomeScreen() {
        mActivity?.navigate(
            MainDirections.toHome,
            NavOptions.Builder().apply {
                setLaunchSingleTop(true)
                setPopUpTo(R.id.action_global_homeFragment, inclusive = false, saveState = false)
            }.build()
        )
    }


    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                mActivity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}