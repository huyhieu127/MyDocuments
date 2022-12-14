package com.huyhieu.library.custom_views.keyboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.library.databinding.ItemKeyboardBinding
import com.huyhieu.library.extensions.context
import com.huyhieu.library.extensions.dimenPx
import com.huyhieu.library.extensions.setTextSizePx

class KeyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var keys = mutableListOf<String>()
    var keyClick: ((key: String) -> Unit)? = null

    fun fillData(keys: MutableList<String>) {
        this.keys = keys
        if (keys.size > 0) {
            notifyItemRangeInserted(0, keys.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vb = ItemKeyboardBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(vb)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is MyViewHolder) return
        holder.bindViewHolder(keys)
    }

    override fun getItemCount() = keys.size

    inner class MyViewHolder(private val vb: ItemKeyboardBinding) : RecyclerView.ViewHolder(vb.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bindViewHolder(keys: MutableList<String>) {
            val key = keys[layoutPosition]
            vb.tvKeyName.text = key
            if (key == "DEL") {
                vb.tvKeyName.setTextSizePx(vb.context.dimenPx(com.intuit.ssp.R.dimen._12ssp))
            } else {
                vb.tvKeyName.setTextSizePx(vb.context.dimenPx(com.intuit.ssp.R.dimen._18ssp))
            }
            vb.root.isInvisible = key == "."
            vb.root.setOnClickListener {
                keyClick?.invoke(key)
            }
        }
    }
}