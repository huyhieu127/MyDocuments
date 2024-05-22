package com.huyhieu.mydocuments.ui.fragments.navigation.home.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.LayoutItemMenuBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.context
import com.huyhieu.mydocuments.libraries.extensions.setOnClickMyListener
import com.huyhieu.mydocuments.models.MenuForm

class MenuElementAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lstData: MutableList<MenuForm>? = null
    var itemClick: ((MenuForm) -> Unit)? = null

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

    inner class MyViewHolder(private val vb: LayoutItemMenuBinding) :
        RecyclerView.ViewHolder(vb.root) {
        fun bindViewHolder() {
            lstData?.get(layoutPosition)?.let { item ->
                vb.apply {
                    imgThumbnail.isVisible = false
                    root.setBackgroundColor(context.color(R.color.white_80))
                    root.setOnClickMyListener {
                        itemClick?.invoke(item)
                    }
                    tvTitle.text = item.name
                    tvNote.text = item.note
                }
            }
        }
    }
}