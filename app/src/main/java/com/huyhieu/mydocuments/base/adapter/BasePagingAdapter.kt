package com.huyhieu.mydocuments.base.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T : Any> : PagingDataAdapter<T, RecyclerView.ViewHolder>,
    BaseAdapter<T, BasePagingAdapter<T>> {

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback)

    /**
     * [PagingDataAdapter] implements
     */
    override fun getItemCount(): Int {
        return getBaseItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return getBaseItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onBaseCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBaseBindViewHolder(viewHolder, position)
    }

    /**
     * [BaseAdapter] implements
     */
    override var onItemClick: OnItemClick<T> = null

    override var onItemViewClick: OnItemViewClick<T> = null

    override var onItemPositionClick: OnItemPositionClick<T> = null

    override var onItemViewLongClick: OnItemViewClick<T> = null

    override var onListChanged: (() -> Unit)? = null

    override var placeHolderItemCount: Int = 0

    override var onFooterIndexChanged: ((Int) -> Unit)? = null

    override var lastBindIndex: Int = -1

    override fun listItem(): List<T> {
        return snapshot().items
    }

}

