package com.huyhieu.mydocuments.ui.fragments.tiktok

import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.base.BaseSimpleAdapter
import com.huyhieu.mydocuments.databinding.ItemTikTokPlayerBinding
import com.huyhieu.mydocuments.models.TikTokVideoForm
import com.huyhieu.mydocuments.utils.load

class TikTokPlayerAdapter : BaseSimpleAdapter<ItemTikTokPlayerBinding, TikTokVideoForm>() {
    override fun ItemTikTokPlayerBinding.onBindMyViewHolder(
        holder: RecyclerView.ViewHolder,
        item: TikTokVideoForm,
        position: Int
    ) {
        //imgThumbnail.alpha = if (item.isPlaying) 0F else 1F
        if (item.thumbnail != null) {
            imgThumbnail.load(item.thumbnail){
                centerCrop()
            }
        } else {
            imgThumbnail.load(item.url)
        }
    }
}