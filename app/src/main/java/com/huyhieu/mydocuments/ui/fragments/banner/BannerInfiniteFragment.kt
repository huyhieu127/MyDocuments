package com.huyhieu.mydocuments.ui.fragments.banner

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentBannerInfiniteBinding
import com.huyhieu.mydocuments.utils.logDebug
import kotlin.math.absoluteValue

class BannerInfiniteFragment : BaseFragment<FragmentBannerInfiniteBinding>() {
    private val bannerAdapter = BannerAdapter()
    val lstData = mutableListOf(1, 2)
    var lstOld = mutableListOf<Int>()


    override fun initializeBinding() = FragmentBannerInfiniteBinding.inflate(layoutInflater)

    override fun FragmentBannerInfiniteBinding.addControls(savedInstanceState: Bundle?) {
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcv)

        if (lstData.size >= 2) {
            lstOld = lstData.toMutableList()
            //Add to left and right
            lstData.addAll(lstData)
            lstData.addAll(lstData)
        }

        rcv.adapter = BannerAdapter()
        rcv.setHasFixedSize(true)
        bannerAdapter.fillData(lstData)

        rcv.scrollToPosition(lstOld.size)
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var posInsert = lstOld.size - 1
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (lstData.size > 1) {
                    val pos =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (dx < 0 && pos == 1) {
                        //bannerAdapter.insertAll(lstOld, 0)
                        bannerAdapter.insert(0, lstOld[posInsert])
                        posInsert--
                        if (posInsert < 0) {
                            posInsert = lstOld.size - 1
                        }
                        bannerAdapter.removeLast()
                        logDebug(posInsert.toString())
                    } else if (dx > 0 && pos == bannerAdapter.itemCount - 2) {
                        //bannerAdapter.insertAll(lstOld)
                        val posRight = (posInsert - (lstOld.size - 1)).absoluteValue
                        bannerAdapter.insert(item = lstOld[posRight])
                        posInsert++
                        if (posInsert == lstOld.size) {
                            posInsert = 0
                        }
                        bannerAdapter.removeFirst()
                        logDebug(posInsert.toString())
                    }
                }
            }
        })
    }

    override fun FragmentBannerInfiniteBinding.addEvents(savedInstanceState: Bundle?) {
    }

}