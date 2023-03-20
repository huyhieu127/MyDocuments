package com.huyhieu.mydocuments.ui.fragments.components.rcv_swipe

import android.annotation.SuppressLint
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.databinding.ItemNotificationSwipeBinding

class NotificationSwipeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listItems = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemNotificationSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bindViewHolder(listItems)
    }

    inner class MyViewHolder(val view: ItemNotificationSwipeBinding) : RecyclerView.ViewHolder(view.root), View.OnDragListener {
        private var xFront = 0
        private var xBack = 0

        @SuppressLint("ClickableViewAccessibility")
        fun bindViewHolder(listItems: MutableList<Int>) {
            view.layoutContentFront.setOnDragListener(this)
        }

        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            logDebug(">>>>>>>>>>>>>>> ${event?.action}")
            when (event?.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.layoutContentFront.translationX = event.x
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.layoutContentFront.translationX = event.x
                }
            }
            return false
        }
    }
}