package com.huyhieu.mydocuments.ui.fragments.multipleapi

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMultipleApiBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MultipleApiFragment : BaseFragment<FragmentMultipleApiBinding>() {

    @Inject
    lateinit var viewModel: MultipleApiVM

    override fun FragmentMultipleApiBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        //callAPI("")
        handleViewClick(
            btnSync,
            btnAsync,
        )
    }

    override fun FragmentMultipleApiBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnSync -> {
                tvResult.text = "STATUS\n"
                viewModel.fetchSyncPokemonDetail()
            }
            btnAsync -> {
                tvResult.text = "STATUS"
                viewModel.fetchAsyncPokemonDetail()
            }
        }
    }

    override fun FragmentMultipleApiBinding.handleLiveData(savedInstanceState: Bundle?) {
        viewModel.loadingState.observe(viewLifecycleOwner) {
            it ?: return@observe
            progressBar.isVisible = it.isLoading
            if (it.tag == "sync") {
                btnSync.setLoading(it.isLoading)
            }
            if (it.tag == "async") {
                btnAsync.setLoading(it.isLoading)
            }
        }
        viewModel.fetchAsyncPokemonDetailLD.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            var sumApi = ""
            var sumSize = 0
            result.onEach {
                val api = it.key
                val size = it.value?.results?.size ?: 0
                sumApi += "\n\t\t\t\t\t+ $api($size)\n"
                sumSize += size
            }
            val response = "STATUS" +
                    "\n\n- Success:" +
                    "\n\n\t\t\t\t\t${sumApi.trim()}" +
                    "\n\n- Total($sumSize)"
            logDebug(response)
            tvResult.text = response
        }
        viewModel.fetchSyncPokemonDetailLD.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            var logText = tvResult.text.toString()
            logText += "\n\t- OK: ${result.first}(${result.second?.results?.size})\n"
            logDebug(logText)
            tvResult.text = logText
        }
    }
}