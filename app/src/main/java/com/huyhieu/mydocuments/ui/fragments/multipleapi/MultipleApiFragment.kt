package com.huyhieu.mydocuments.ui.fragments.multipleapi

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMultipleApiBinding
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MultipleApiFragment : BaseFragment<FragmentMultipleApiBinding>() {

    @Inject
    lateinit var viewModel: MultipleApiVM

    override fun FragmentMultipleApiBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        //callAPI("")
        viewModel.fetchPokemonDetail()
    }

    override fun FragmentMultipleApiBinding.handleLiveData(savedInstanceState: Bundle?) {
        viewModel.viewModelScope.launch {
            viewModel.resultAPI.collectLatest {
                it ?: return@collectLatest
                logDebug(it.results?.size?.toString())
            }
        }
        viewModel.loadingState.observe(viewLifecycleOwner) {
            it ?: return@observe
            progressBar.isVisible = it.isLoading
        }
        viewModel.fetchPokemonDetailLD.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            var sumApi = ""
            var sumSize = 0
            result.onEach {
                val api = it.key
                val size = it.value?.results?.size ?: 0
                sumApi += " $api: $size "
                sumSize += size
            }
            val response = "SUCCESS:$sumApi\nTotal size: $sumSize"
            logDebug(response)
            tvResult.text = response
        }
    }

    override fun FragmentMultipleApiBinding.callAPI(
        apiKey: String,
        param: Any?,
        function: ((resultData: Any?) -> Unit)?
    ) {
        var result = ""
        viewModel.getPokemons().observe(viewLifecycleOwner) {
            when (it.statusPokeAPI) {
                ResultPokeAPI.StatusPokeAPI.LOADING -> {
                    logDebug(" - LOADING")
                    progressBar.isVisible = true
                }
                ResultPokeAPI.StatusPokeAPI.COMPLETE -> {
                    logDebug(" - COMPLETE")
                    progressBar.isVisible = false
                }
                ResultPokeAPI.StatusPokeAPI.NETWORK -> logDebug(" - NETWORK")
                ResultPokeAPI.StatusPokeAPI.ERROR -> logDebug(" - ERROR: ${it.message}")
                ResultPokeAPI.StatusPokeAPI.SUCCESS -> {
                    val response =
                        "<${it.apiKey}> - SUCCESS: ${it.response?.results?.first()?.name}, size: ${it.response?.results?.size}"
                    logDebug(response)
                    result = (result + "\n" + response)
                    tvResult.text = result
                }
            }
        }
    }
}