package com.huyhieu.mydocuments.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*

abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder>,
    BaseAdapter<T, BaseListAdapter<T>> {

    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncListDiffer(itemCallback)
    }

    /**
     * [ListAdapter] implements
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

    override fun getCurrentList(): MutableList<T> {
        return differ.currentList
    }

    override fun submitList(list: MutableList<T>?) {
        differ.submitList(list)
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        differ.submitList(list, commitCallback)
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

    override fun listItem(): MutableList<T> {
        return differ.currentList
    }

    /**
     * Utils
     */
    open fun submit() {
        set(currentList)
    }

    open fun set(collection: Collection<T>?, callback: Runnable? = null): BaseListAdapter<T> {
        lastBindIndex = -1
        submitList(collection?.toMutableList()) {
            onListChanged()
            callback?.run()
        }
        return this
    }

    open fun set(array: Array<T>?, callback: Runnable? = null): BaseListAdapter<T> {
        set(collection = array?.toMutableList())
        return this
    }

    open fun setElseEmpty(col: Collection<T>?, callback: Runnable? = null): BaseListAdapter<T> {
        if (col.isNullOrEmpty()) return this
        return set(col, callback)
    }

    open fun setElseEmpty(array: Array<T>?, callback: Runnable? = null): BaseListAdapter<T> {
        if (array.isNullOrEmpty()) return this
        return set(array, callback)
    }

    open fun setElseEmpties(vararg elements: T?): BaseListAdapter<T> {
        return setElseEmpty(elements.toList().filterNotNull())
    }

    open fun remove(item: T, callback: Runnable? = null) {
        val list = listItem().toMutableList()
        list.remove(item)
        set(list, callback)
    }

    open fun clear(callback: Runnable? = null) {
        set(listOf(), callback)
    }

    private fun asyncListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncListDiffer<T> {
        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position, if (hasFooter) count + 1 else count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition, toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                if (position == 0 && blankItemOptions() != null) {
                    adapterCallback.onChanged(position, if (hasFooter) count + 1 else count, null)
                } else {
                    adapterCallback.onInserted(position, if (hasFooter) count + 1 else count)
                }
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position, if (hasFooter) count + 1 else count)
            }
        }
        return AsyncListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

}