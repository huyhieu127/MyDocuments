package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSwipeToRefreshBinding
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SwipeToRefreshFragment : BaseFragment<FragmentSwipeToRefreshBinding>(), ISwipeToRefresh {
    val adapter by lazy { SwipeToRefreshAdapter() }
    private var offset = 20
    private var page = 1

    override var isRefreshing: Boolean = false
    private var jobRefresh: Job? = null
    override fun onRefreshing(isRefreshing: Boolean) {
        if (isRefreshing && jobRefresh == null) {
            jobRefresh = lifecycleScope.launch {
                delay(2000)
                if (this@SwipeToRefreshFragment.isRefreshing) {
                    setHiddenRefresh()
                }
                jobRefresh?.cancel()
                jobRefresh = null
            }
        }
    }

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        requireActivity().setDarkColorStatusBar(true)
        setupRecyclerView()
        fakeFillData(page)
    }

    private fun fakeFillData(page: Int) {
        adapter.listItems = (1..offset * this.page).toMutableList()
    }
}