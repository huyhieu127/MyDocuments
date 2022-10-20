package com.huyhieu.mydocuments.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BlankAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(val bind: ViewBinding) : RecyclerView.ViewHolder(bind.root)

    override fun getItemCount(): Int {
        viewBinding ?: return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(viewBinding!!(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindItem?.invoke((viewHolder as ViewHolder).bind)
    }

    private var viewBinding: ((LayoutInflater, ViewGroup, Boolean) -> ViewBinding)? = null

    fun viewBinding(block: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding): BlankAdapter {
        viewBinding = block
        return this
    }

    private var onBindItem: ((ViewBinding) -> Unit)? = null

    fun <T : ViewBinding> onBindItem(block: (T) -> Unit): BlankAdapter {
        onBindItem = { block(it as T) }
        return this
    }

    fun bind(v: RecyclerView): BlankAdapter {
        v.layoutManager = LinearLayoutManager(v.context)
        v.adapter = this
        return this
    }

}