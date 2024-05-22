package com.huyhieu.mydocuments.ui.fragments.navigation.home.components

import android.R
import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.databinding.LayoutItemMenuBinding
import com.huyhieu.mydocuments.libraries.extensions.context
import com.huyhieu.mydocuments.libraries.extensions.drawable
import com.huyhieu.mydocuments.libraries.extensions.setOnClickMyListener
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

        private val elementAdapter by lazy { MenuElementAdapter() }

        fun bindViewHolder() {
            lstData?.get(layoutPosition)?.let { item ->
                vb.apply {
                    imgArrowRight.rotation = (0f)
                    rcvElements.isVisible = false
                    root.foreground = if (item.elements.isEmpty())
                        context.drawable(root.selectableItemBackgroundBorderless())
                    else null
                    if (item.elements.isNotEmpty()) {
                        root.foreground = null
                        rcvElements.adapter = elementAdapter
                        elementAdapter.fillData(item.elements.toMutableList())
                        elementAdapter.itemClick = { element ->
                            itemClick?.invoke(element)
                        }
                    }

                    root.setOnClickMyListener {
                        if (item.elements.isEmpty()) {
                            itemClick?.invoke(item)
                        } else {
                            val isExpanded = rcvElements.isVisible
                            if (!isExpanded) {
                                imgArrowRight.animate().rotation(90f).start()
                            } else {
                                imgArrowRight.animate().rotation(0f).start()
                            }
                            rcvElements.isVisible = !isExpanded
                        }
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

        private fun LayoutItemMenuBinding.createRecyclerView(): RecyclerView {
            val recyclerView = RecyclerView(root.context)

            val lp = ConstraintLayout.LayoutParams(constraintLayout.layoutParams).apply {
                width = 0
                height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                topToBottom = vb.divider.id
                startToStart = vb.tvTitle.id
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            recyclerView.apply {
                layoutParams = lp
                layoutManager = LinearLayoutManager(vb.root.context, RecyclerView.VERTICAL, false)
                isVisible = false
            }
            return recyclerView
        }

        private fun View.selectableItemBackgroundBorderless() = with(TypedValue()) {
            context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, this, true)
            return@with resourceId
        }
    }
}