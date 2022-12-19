package com.huyhieu.mydocuments.ui.fragments.home.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.library.extensions.setOnClickMyListener
import com.huyhieu.mydocuments.databinding.LayoutItemMenuBinding
import com.huyhieu.mydocuments.models.MenuForm

class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lstData: MutableList<MenuForm>? = null

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
                    root.setOnClickMyListener {
                       item.onNavigate?.invoke()
                    }
                    tvTitle.text = item.name
                }
            }
        }
    }
}