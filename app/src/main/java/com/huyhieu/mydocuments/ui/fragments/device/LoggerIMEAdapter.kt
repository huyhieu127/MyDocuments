package com.huyhieu.mydocuments.ui.fragments.device

import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseSimpleAdapter
import com.huyhieu.mydocuments.databinding.ItemLoggerImeBinding

class LoggerIMEAdapter : BaseSimpleAdapter<ItemLoggerImeBinding, DeviceInfo>() {
    override fun ItemLoggerImeBinding.onBindMyViewHolder(
        holder: RecyclerView.ViewHolder, item: DeviceInfo, position: Int
    ) {
        tvTitle.text = item.title
        tvInfo.text = item.info
    }
}

class DeviceInfo(
    var title: String = "",
    var info: String = "",
)