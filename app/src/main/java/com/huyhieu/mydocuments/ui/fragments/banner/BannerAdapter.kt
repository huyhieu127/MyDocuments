package com.huyhieu.mydocuments.ui.fragments.banner

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lstData = mutableListOf<Int>()
    var itemClick: ((type: Int) -> Unit)? = null

    fun fillData(lstData: MutableList<Int>) {
        this.lstData.addAll(lstData)
        notifyItemRangeChanged(0, lstData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.bindViewHolder()
        }
    }

    override fun getItemCount() = lstData.size

    inner class MyViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder() {
            lstData[layoutPosition].let { item ->
                binding.apply {
                    root.text = item.toString()
                    if (item % 2 == 0) {
                        root.setBackgroundColor(Color.RED)
                    } else {
                        root.setBackgroundColor(Color.GREEN)
                    }
                }
            }
        }
    }

    fun removeLast() {
        lstData.removeLast()
        notifyItemRemoved(lstData.size)
    }

    fun removeFirst() {
        lstData.removeFirst()
        notifyItemRemoved(0)
    }

    fun insert(position: Int = -1, item: Int?) {
        item ?: return
        if (position != -1) {
            lstData.add(position, item)
            notifyItemInserted(position)
        } else {
            lstData.add(item)
            notifyItemInserted(lstData.size)
        }
    }

    fun insertAll(position: Int = -1, collection: Collection<Int>?) {
        collection ?: return
        if (position == -1) {
            val posOld = lstData.size - 1
            lstData.addAll(collection)
            notifyItemRangeInserted(posOld, collection.size)
        } else {
            lstData.addAll(position, collection)
            notifyItemRangeInserted(position, collection.size)
        }
    }

    fun removeRange(collection: Collection<Int>?, position: Int = -1) {
        collection ?: return
        //lstData.remove
        if (position == -1) {
            val posOld = lstData.size - 1
            lstData.addAll(collection)
            notifyItemRangeInserted(posOld, collection.size)
        } else {
            lstData.addAll(position, collection)
            notifyItemRangeInserted(position, collection.size)
        }
    }
}