package com.huyhieu.widget.commons.calendar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.huyhieu.library.extensions.color
import com.huyhieu.library.extensions.colorStateList
import com.huyhieu.library.extensions.setOnClickMyListener
import com.huyhieu.widget.R
import com.huyhieu.widget.commons.calendar.DayForm
import com.huyhieu.widget.commons.calendar.MonthForm
import com.huyhieu.widget.databinding.WidgetCalendarDayOfMonthBinding

class DaysOfCalendarAdapter : RecyclerView.Adapter<ViewHolder>() {

    var dayClick: ((DayForm) -> Unit)? = null
    var onDaySelected: ((DaysOfCalendarAdapter) -> Unit)? = null
    private var lstDays = mutableListOf<DayForm>()
    private lateinit var monthForm: MonthForm
    var daySelected = ""
    var idxDaySelected = -1
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

        private val context: Context by lazy { binding.root.context }

        fun bindViewHolder(lstDays: MutableList<DayForm>) {
            binding.apply {
                val item = lstDays[layoutPosition]
                tvDay.text = item.day
                //Set selected
                if (item.isSelected) {
                    daySelected = item.date
                    idxDaySelected = layoutPosition
                    root.isSelected = true
                    onDaySelected?.invoke(this@DaysOfCalendarAdapter)
                } else {
                    root.isSelected = false
                }
                //Set color selected
                if (item.isToday && !root.isSelected) {
                    tvDay.setTextColor(context.color(R.color.colorPrimary))
                } else if (!item.isDayAvailable()) {
                    tvDay.setTextColor(context.color(R.color.colorGrayscaleDisable))
                } else {
                    tvDay.setTextColor(context.colorStateList(R.color.color_selected_black_white))
                }
                //Set gesture
                if (item.isDayAvailable()) {
                    root.setOnClickMyListener {
                        if (idxDaySelected != -1) {
                            lstDays[idxDaySelected].isSelected = false
                            notifyItemChanged(idxDaySelected)
                        }
                        item.isSelected = true
                        notifyItemChanged(layoutPosition)
                        dayClick?.invoke(item)
                    }
                }
            }
        }
    }

    fun fillListDays(lstDays: MutableList<DayForm>) {
        this.lstDays = lstDays
        notifyItemRangeChanged(0, itemCount)
    }

    fun updateDayFromOtherMonth() {
        if (idxDaySelected != -1) {
            lstDays[idxDaySelected].isSelected = false
            notifyItemChanged(idxDaySelected)
        }
    }
}