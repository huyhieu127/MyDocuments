package com.huyhieu.mydocuments.ui.fragments.navigation.home.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.library.extensions.setOnClickMyListener
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.databinding.LayoutItemMenuBinding
import com.huyhieu.mydocuments.models.HomeMenu
import com.huyhieu.mydocuments.models.MenuForm

class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                    root.setOnClickMyListener {
                        itemClick?.invoke(item)
                    }
                    tvTitle.text = item.name
                    vb.setNote(item)
                }
            }
        }

        private fun LayoutItemMenuBinding.setNote(item: MenuForm) {
            when (item.type) {
                HomeMenu.MENU_ABOUT -> {
                    tvNote.text = String.format("${BuildConfig.BUILD_TYPE.uppercase()}_${BuildConfig.VERSION_NAME}")
                }
                else -> {
                    tvNote.text = item.note
                }
            }
        }
    }
}