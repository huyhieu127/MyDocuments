package com.huyhieu.mydocuments.ui.fragments.my_tube

import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseSimpleAdapter
import com.huyhieu.mydocuments.databinding.ItemMyTubeBinding
import com.huyhieu.mydocuments.models.MyTubeForm
import com.huyhieu.mydocuments.utils.load

class MyTubeAdapter : BaseSimpleAdapter<ItemMyTubeBinding, MyTubeForm>() {
    override fun ItemMyTubeBinding.onBindMyViewHolder(
        holder: RecyclerView.ViewHolder,
        item: MyTubeForm,
        position: Int
    ) {
        if (item.urlThumbnail.isNotEmpty()) {
            imgThumbnail.load(item.urlThumbnail)
        }
        tvTitle.text = item.title

        if (item.urlAuthorAvatar.isNotEmpty()) {
            imgAuthorAvatar.load(item.urlAuthorAvatar)
        }
        tvDescribe.text = "${item.authorName} • 400 N lượt xem • 2 tuần trước"
    }
}