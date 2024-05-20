package com.huyhieu.mydocuments.ui.fragments.content_provider

import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseSimpleAdapter
import com.huyhieu.mydocuments.databinding.ItemUserContentProviderBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.context
import com.huyhieu.mydocuments.models.User

class UserCPAdapter : BaseSimpleAdapter<ItemUserContentProviderBinding, User>() {
    override fun ItemUserContentProviderBinding.onBindMyViewHolder(
        holder: RecyclerView.ViewHolder, item: User, position: Int
    ) {
        root.setBackgroundColor(
            if (item.isSelected) context.color(R.color.colorRedAlertMiddle)
            else context.color(R.color.white)
        )
        tvId.text = item.id.toString()
        tvName.text = item.name
        "Age: ${item.age}".let { tvAge.text = it }

        root.setOnClickListener {
            if (posOld != -1 && posOld < listData.size) {
                listData[posOld].isSelected = false
                notifyItemChanged(posOld)
            }
            item.isSelected = true
            posOld = position
            notifyItemChanged(position)
            onClickItemWithPosition?.invoke(position, item)
        }
    }
}
