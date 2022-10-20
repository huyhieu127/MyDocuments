package com.huyhieu.mydocuments.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

abstract class BaseSelectableAdapter<T>(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) :
    BaseListAdapter<T>(itemCallback) {

    final override var onItemClick: OnItemClick<T> = { item ->
        updateSelectedItem(item)
    }

    override fun onItemClick(block: OnItemClick<T>): BaseListAdapter<T> {
        return super.onItemClick(object : OnItemClick<T> {
            override fun invoke(p1: T) {
                updateSelectedItem(p1)
                block?.invoke(p1)
            }

        })
    }

    override fun itemClickDelayed(): Long {
        return 0
    }

    final override fun ViewBinding.onBindModelItem(item: T, position: Int) {
        onBindDefaultItem(item, position)
        if (isSelected(item)) {
            onBindSelectedItem(item, position)
        } else {
            onBindUnselectedItem(item, position)
        }
    }

    abstract fun ViewBinding.onBindDefaultItem(item: T, position: Int)

    abstract fun ViewBinding.onBindSelectedItem(item: T, position: Int)

    abstract fun ViewBinding.onBindUnselectedItem(item: T, position: Int)

    open fun isSelected(item: T?): Boolean {
        return areSameItems(item, selectedItem)
    }

    open fun areSameItems(item: T?, other: T?): Boolean {
        return item == other && selectedItem != null
    }

    private var mSelectedItem: T? = null

    var selectedItem: T?
        get() = mSelectedItem
        set(value) {
            val oldPosition: Int = listItem().indexOf(selectedItem)
            mSelectedItem = value
            if (oldPosition in 0..lastIndex) {
                notifyItemChanged(oldPosition)
            }
            val newPosition = listItem().indexOf(value)
            if (newPosition in 0..lastIndex) {
                notifyItemChanged(newPosition)
            }
        }

    val selectedPosition: Int
        get() {
            selectedItem ?: return -1
            return listItem().indexOf(selectedItem)
        }

    private val itemClickList = mutableListOf<(T, Boolean) -> Unit>()

    var onSelectionChanged: OnItemClick<T> = null

    fun onSelectionChanged(block: (T) -> Unit): BaseSelectableAdapter<T> {
        onSelectionChanged = block
        return this
    }

    fun addOnItemClick(block: (T, Boolean/*hasChange*/) -> Unit): BaseSelectableAdapter<T> {
        itemClickList.add(block)
        return this
    }

    fun select(item: T?, hasNotify: Boolean = true) {
        if (hasNotify) {
            selectedItem = item
        } else {
            mSelectedItem = item
        }
    }

    private fun updateSelectedItem(item: T) {
        if (isSelected(item)) {
            itemClickList.forEach { it(item, false) }
            return
        }
        selectedItem = item
        onSelectionChanged?.invoke(item)
        itemClickList.forEach { it(item, true) }
    }

}