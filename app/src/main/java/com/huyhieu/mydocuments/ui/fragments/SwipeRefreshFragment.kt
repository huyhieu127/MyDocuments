package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSwipeRefreshBinding

class SwipeRefreshFragment : BaseFragment<FragmentSwipeRefreshBinding>() {
    override fun initializeBinding() = FragmentSwipeRefreshBinding.inflate(layoutInflater)

    override fun FragmentSwipeRefreshBinding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentSwipeRefreshBinding.addEvents(savedInstanceState: Bundle?) {
    }
}