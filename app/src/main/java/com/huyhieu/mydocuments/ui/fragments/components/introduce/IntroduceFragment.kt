package com.huyhieu.mydocuments.ui.fragments.components.introduce

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentIntroduceBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.clearBackStack
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate

class IntroduceFragment : BaseFragment<FragmentIntroduceBinding>() {
    private val myViewPagerAdapter by lazy { MyViewPagerAdapter() }
    private var layouts: IntArray? = null

    override fun FragmentIntroduceBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        //hideNavigationBottom()
        layouts = intArrayOf(
            R.layout.fragment_introduce_page1,
            R.layout.fragment_introduce_page2,
            R.layout.fragment_introduce_page3
        )

        vpIntroduce.adapter = myViewPagerAdapter
        vpIntroduce.offscreenPageLimit = 2
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
                //btnNext.isInvisible = position != layouts!!.size - 1
                btnNext.isEnabled = position == layouts!!.size - 1
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        handleViewClick(tvSkip, btnNext)
    }

    private fun FragmentIntroduceBinding.fillText(position: Int) {
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

    override fun FragmentIntroduceBinding.onClickViewBinding(v: View, id: Int) {
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

    private fun getCurrentItem(): Int = vb.vpIntroduce.currentItem + 1

    private fun launchHomeScreen() {
        navigate(MyNavHost.Main, MainDirections.toNavigation) {
            clearBackStack(findNavController(), true)
        }
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