package com.huyhieu.widget.commons.loadmore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.widget.databinding.LayoutItemLoadingBinding

abstract class MyAdapterLoadMore<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var lstData: MutableList<T>? = null
    var page = 1
    var offset = 0
    var limit = 10

    var isEndPage = false

    companion object {
        const val TYPE_LOADING = -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(lstDataNew: MutableList<T>?) {
        if (!lstDataNew.isNullOrEmpty()) {
            if (offset == 0) {
                this.lstData = lstDataNew
                notifyDataSetChanged()
            } else {
                this.lstData?.addAll(lstDataNew)
                notifyItemRangeChanged(offset, itemCount)
                notifyDataSetChanged()
            }
            page++
            offset = lstDataNew.size - 1

            if (lstDataNew.size < limit) {
                showLoading(false)
            }
        } else {
            showLoading(false)
        }
    }

    fun removeItem(position: Int) {
        try {
            if (position > -1 && position < this.lstData?.size ?: 0) {
                notifyItemRemoved(position)
                this.lstData?.removeAt(position)
            }
        } catch (ex: Exception) {
        }
    }

    fun refreshPage() {
        lstData?.clear()
        page = 1
        offset = 0
        isEndPage = false
    }

    private fun showLoading(isShow: Boolean = false) {
        isEndPage = !isShow
        notifyItemChanged(itemCount - 1)
    }

    /**
     * Alternative function for onCreateViewHolder
     * */
    abstract fun onCreateMyViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1 && !isEndPage) {
            return TYPE_LOADING
        }
        return super.getItemViewType(position)
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LOADING) {
            val view =
                LayoutItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(view)
        } else {
            onCreateMyViewHolder(parent, viewType)
        }
    }

    final override fun getItemCount(): Int = lstData?.size ?: 0

    inner class LoadingViewHolder(view: LayoutItemLoadingBinding) :
        RecyclerView.ViewHolder(view.root)
}