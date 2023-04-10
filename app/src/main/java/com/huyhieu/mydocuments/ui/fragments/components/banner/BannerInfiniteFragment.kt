package com.huyhieu.mydocuments.ui.fragments.components.banner

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentBannerInfiniteBinding

class BannerInfiniteFragment : BaseFragment<FragmentBannerInfiniteBinding>() {
    private val bannerAdapter = BannerAdapter()
    val lstData = mutableListOf(1, 2, 3, 4, 5, 6)
    var lstOld = mutableListOf<Int>()
    override fun FragmentBannerInfiniteBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcv)

        if (lstData.size >= 2) {
            lstOld = lstData.toMutableList()
            //Add to left and right
            lstData.addAll(lstData)
            lstData.addAll(lstData)
        }

        rcv.adapter = bannerAdapter
        bannerAdapter.fillData(lstData)

        rcv.scrollToPosition(lstOld.size)
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var posLeft = lstOld.size - 1
            var posEnd = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (lstData.size > 1) {
                    val pos =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (dx < 0 && pos == 1) {
                        //bannerAdapter.insertAll(lstOld, 0)
                        bannerAdapter.insert(0, lstOld[posLeft])
                        posEnd = posLeft
                        posLeft--
                        if (posLeft < 0) {
                            posLeft = lstOld.size - 1
                        }
                        bannerAdapter.removeLast()
                    } else if (dx > 0 && pos == bannerAdapter.itemCount - 2) {
                        //bannerAdapter.insertAll(lstOld)
                        bannerAdapter.insert(item = lstOld[posEnd])
                        posLeft = posEnd
                        posEnd++
                        if (posEnd > lstOld.size - 1) {
                            posEnd = 0
                        }
                        bannerAdapter.removeFirst()
                    }
                }
            }
        })
    }
}