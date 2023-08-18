package com.huyhieu.mydocuments.ui.fragments.you_tube

import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseSimpleAdapter
import com.huyhieu.mydocuments.databinding.ItemYouTubeBinding
import com.huyhieu.mydocuments.models.YouTubeForm
import com.huyhieu.mydocuments.utils.load

class YouTubeAdapter : BaseSimpleAdapter<ItemYouTubeBinding, YouTubeForm>() {
    override fun ItemYouTubeBinding.onBindMyViewHolder(
        holder: RecyclerView.ViewHolder,
        item: YouTubeForm,
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