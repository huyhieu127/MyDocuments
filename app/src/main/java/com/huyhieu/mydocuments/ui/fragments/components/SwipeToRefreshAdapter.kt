package com.huyhieu.mydocuments.ui.fragments.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.databinding.ItemLoadingBinding
import com.huyhieu.mydocuments.databinding.ItemNotificationSwipeBinding

enum class ItemType(type: Int) {
    Refresh(0), Item(1)
}

class SwipeToRefreshAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listItems = mutableListOf<Int>()

//    override fun getItemViewType(position: Int): Int {
//        return if (position == 0) ItemType.Refresh.ordinal else ItemType.Item.ordinal
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        /*when (viewType) {
            ItemType.Refresh.ordinal -> {
                val view =
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RefreshVH(view)
            }

            else -> {
                val view = ItemNotificationSwipeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return MyViewHolder(view)

            }
        }*/

        val view = ItemNotificationSwipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount() = listItems.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> holder.bindViewHolder(listItems)
        }
    }

    inner class RefreshVH(val view: ItemLoadingBinding) : RecyclerView.ViewHolder(view.root)

    inner class MyViewHolder(val view: ItemNotificationSwipeBinding) :
        RecyclerView.ViewHolder(view.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bindViewHolder(listItems: MutableList<Int>) {
            view.tvTitle.text = "$layoutPosition - ${System.currentTimeMillis()}"
        }

    }
}