package com.huyhieu.mydocuments.ui.fragments.components.rcv_swipe

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentNotificationSwipeBinding

class NotificationSwipeFragment : BaseFragment<FragmentNotificationSwipeBinding>() {
    val adapter by lazy { NotificationSwipeAdapter() }
    override fun FragmentNotificationSwipeBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        requireActivity().setDarkColorStatusBar(true)
        rcvNotifications.adapter = adapter
        adapter.listItems = (1..10).toMutableList()
        //adapter.notifyItemRangeInserted(0, adapter.listItems.size)
    }

}