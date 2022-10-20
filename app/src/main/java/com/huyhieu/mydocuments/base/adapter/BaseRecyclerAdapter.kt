package com.huyhieu.mydocuments.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    BaseAdapter<T, BaseRecyclerAdapter<T>> {

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
     *
     */
    protected var currentList: MutableList<T> = mutableListOf()

    override var onItemClick: OnItemClick<T> = null

    override var onItemViewClick: OnItemViewClick<T> = null

    override var onItemPositionClick: OnItemPositionClick<T> = null

    override var onItemViewLongClick: OnItemViewClick<T> = null

    override var onListChanged: (() -> Unit)? = null

    override var placeHolderItemCount: Int = 0

    override var onFooterIndexChanged: ((Int) -> Unit)? = null

    override var lastBindIndex: Int = -1

    override fun listItem(): MutableList<T> {
        return currentList
    }

    /**
     * Utils
     */
    open fun set(collection: Collection<T>?): BaseRecyclerAdapter<T> {
        currentList = collection?.toMutableList() ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
        return this
    }

    open fun set(array: Array<T>?): BaseRecyclerAdapter<T> {
        currentList = array?.toMutableList() ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
        return this
    }

    open fun setElseEmpty(collection: Collection<T>?): BaseRecyclerAdapter<T> {
        if (collection.isNullOrEmpty()) return this
        return set(collection)
    }

    open fun setElseEmpty(array: Array<T>?): BaseRecyclerAdapter<T> {
        if (array == null || array.isEmpty()) return this
        return set(array)
    }

    open fun setElseEmpties(vararg elements: T?): BaseRecyclerAdapter<T> {
        return setElseEmpty(elements.toList().filterNotNull())
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList.addAll(collection)
        notifyDataSetChanged()
    }

    open fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        notifyItemRangeChanged(size, size + 1)
    }

    open fun add(position: Int, model: T?) {
        model ?: return
        currentList.add(position, model)
        notifyDataSetChanged()
    }

    open fun removeLast() {
        currentList.removeLast()
        notifyItemRemoved(currentList.size)
    }

    open fun removeFirst() {
        currentList.removeFirst()
        notifyItemRemoved(0)
    }

    open fun insert(position: Int = -1, model: T?) {
        model ?: return
        if (position != -1) {
            currentList.add(position, model)
            notifyItemInserted(position)
        } else {
            currentList.add(model)
            notifyItemInserted(currentList.size)
        }
    }

    open fun insertAll(position: Int = -1, collection: Collection<T>?) {
        collection ?: return
        if (position == -1) {
            val posOld = currentList.size - 1
            currentList.addAll(collection)
            notifyItemRangeInserted(posOld, collection.size)
        } else {
            currentList.addAll(position, collection)
            notifyItemRangeInserted(position, collection.size)
        }
    }

    open fun removeRange(collection: Collection<T>?, position: Int = -1) {
        collection ?: return
        //currentList.remove
        if (position == -1) {
            val posOld = currentList.size - 1
            currentList.addAll(collection)
            notifyItemRangeInserted(posOld, collection.size)
        } else {
            currentList.addAll(position, collection)
            notifyItemRangeInserted(position, collection.size)
        }
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList.indexOf(model)
        remove(position)
    }

    open fun clear() {
        currentList = mutableListOf()
        notifyDataSetChanged()
    }

}