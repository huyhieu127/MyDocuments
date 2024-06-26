package com.huyhieu.mydocuments.ui.fragments.network.recallapi

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentRecallApiBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecallAPIFragment : BaseFragment<FragmentRecallApiBinding>() {

    @Inject
    lateinit var recallAPIVM: RecallAPIVM

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        setClickViews(btnRetry)
        recallAPIVM.fetchUser()
    }

    override fun FragmentRecallApiBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnRetry -> {
                recallAPIVM.fetchUser()
            }
        }
    }

    override fun onMyLiveData(savedInstanceState: Bundle?) = with(vb) {
        recallAPIVM.loadingState.observe(viewLifecycleOwner) {
            prgLoading.isVisible = it.isLoading
        }
        recallAPIVM.userLD.observe(viewLifecycleOwner) {
            logDebug(it.toString())
            it ?: return@observe
            val url = it.get("avatar").asString
            val name = ("${it.get("first_name").asString} ${it.get("last_name").asString}")
            Glide.with(mActivity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgPokemon)
            tvNamePokemon.text = name
        }
    }
}