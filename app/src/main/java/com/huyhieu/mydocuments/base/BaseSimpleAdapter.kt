package com.huyhieu.mydocuments.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.utils.tryCatch
import java.lang.reflect.ParameterizedType

abstract class BaseSimpleAdapter<VB : ViewBinding, DATA> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listData = mutableListOf<DATA>()
        private set

    var onClickItem: ((item: DATA) -> Unit)? = null
    var onClickItemWithPosition: ((position: Int, item: DATA) -> Unit)? = null
    var posOld: Int = -1
    /**
     * Configs
     * */
    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val vb: VB = getViewBinding(LayoutInflater.from(parent.context), parent)
        return MyViewHolder(vb)
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseSimpleAdapter<*, *>.MyViewHolder) {
            holder.bindViewHolder()
        }
    }

    final override fun getItemCount() = listData.size

    private inner class MyViewHolder(val vb: VB) : RecyclerView.ViewHolder(vb.root) {
        fun bindViewHolder() {
            tryCatch {
                val item = listData[layoutPosition]
                vb.root.setOnClickListener {
                    onClickItem?.invoke(item)
                    onClickItemWithPosition?.invoke(layoutPosition, item)
                    posOld = layoutPosition
                }
                vb.onBindMyViewHolder(holder = this, item = item, position = layoutPosition)
            }
        }
    }

    /**
     * Interactions
     * */

    abstract fun VB.onBindMyViewHolder(holder: RecyclerView.ViewHolder, item: DATA, position: Int)

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSet() {
        posOld = -1
        notifyDataSetChanged()
    }

    fun fillData(listData: MutableList<DATA>) {
        this.listData = listData
        tryCatch {
            notifyDataSet()
            //notifyItemRangeInserted(0, listData.size - 1)
        }
    }

    fun passData(listData: MutableList<DATA>) {
        this.listData = listData
    }

    fun addNewField(listData: MutableList<DATA>, index: Int = -1) {
        tryCatch {
            if (index != -1)
                this.listData.addAll(index, listData)
            else
                this.listData.addAll(listData)
            notifyItemInserted(this.listData.size - 1)
        }
    }

    fun addNewField(data: DATA, index: Int = -1) {
        tryCatch {
            if (index != -1)
                this.listData.add(index, data)
            else
                this.listData.add(data)
            notifyItemInserted(this.listData.size - 1)
        }
    }

    fun updateField(position: Int, data: DATA) {
        tryCatch {
            this.listData[position] = data
            notifyItemChanged(position)
        }
    }

    fun removeField(position: Int) {
        tryCatch {
            this.listData.removeAt(position)
            notifyItemRemoved(position)
            posOld = -1
            if (position == 0) notifyDataSet()
        }
    }

    /**
     * Methods
     * */
    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return method.invoke(null, inflater, container, false) as VB
    }
}