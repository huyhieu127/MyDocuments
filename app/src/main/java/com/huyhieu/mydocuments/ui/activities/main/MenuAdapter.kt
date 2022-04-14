package com.huyhieu.mydocuments.ui.activities.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.databinding.LayoutItemMenuBinding
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.others.enums.MenuType

class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lstData: MutableList<MenuForm>? = null
    var itemClick: ((type: MenuType) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun fillData(lstData: MutableList<MenuForm>) {
        this.lstData = lstData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            LayoutItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.bindViewHolder()
        }
    }

    override fun getItemCount() = lstData?.size ?: 0

    inner class MyViewHolder(val binding: LayoutItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder() {
            lstData?.get(layoutPosition)?.let { item ->
                binding.apply {
                    root.setOnClickListener {
                        itemClick?.invoke(item.type)
                    }
                    tvTitle.text = item.name
                }
            }
        }
    }
}