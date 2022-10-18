package com.huyhieu.mydocuments.ui.fragments.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.databinding.WidgetCalendarDayOfMonthBinding
import com.huyhieu.mydocuments.ui.fragments.calendar.DayForm
import com.huyhieu.mydocuments.ui.fragments.calendar.MonthForm
import com.huyhieu.mydocuments.utils.extensions.color
import com.huyhieu.mydocuments.utils.extensions.setOnClickMyListener

class DaysOfCalendarAdapter : RecyclerView.Adapter<ViewHolder>() {

    var dayClick: ((DayForm) -> Unit)? = null
    private var lstDays = mutableListOf<DayForm>()
    private lateinit var monthForm: MonthForm
    private var daySelected = ""
    private var idxSelected = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WidgetCalendarDayOfMonthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder !is DayViewHolder) return
        holder.bindViewHolder(lstDays)
    }

    override fun getItemCount(): Int = lstDays.size

    inner class DayViewHolder(val binding: WidgetCalendarDayOfMonthBinding) :
        ViewHolder(binding.root) {
        fun bindViewHolder(lstDay: MutableList<DayForm>) {
            binding.apply {
                val item = lstDay[layoutPosition]
                if (item.isDayOfPrevMonth || item.isDayOfNextMonth) {
                    tvDay.setTextColor(root.context.color(R.color.colorGrayscaleDisable))
                } else {
                    //tvDay.setTextColor(root.context.color(R.color.color_selected_black_white))
                    root.setOnClickMyListener {
                        daySelected = item.date
                        if (idxSelected != -1) {
                            lstDay[idxSelected].isSelected = false
                            notifyItemChanged(idxSelected)
                        }
                        notifyItemChanged(layoutPosition)

                        dayClick?.invoke(item)
                    }
                }
                tvDay.text = item.day

                if (item.isSelected || (daySelected == item.date && daySelected.isNotEmpty())) {
                    daySelected = item.date
                    idxSelected = layoutPosition
                    root.isSelected = true
                } else {
                    root.isSelected = false
                }

            }
        }
    }

    fun fillListDays(lstDays: MutableList<DayForm>) {
        this.lstDays = lstDays
        notifyItemRangeChanged(0, itemCount)
    }
}