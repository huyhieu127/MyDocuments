package com.huyhieu.mydocuments.ui.fragments.multipleapi

import android.os.Bundle
import androidx.core.view.isVisible
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMultipleApiBinding
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MultipleApiFragment : BaseFragment<FragmentMultipleApiBinding>() {

    @Inject
    lateinit var viewModel: MultipleApiVM

    override fun initializeBinding() = FragmentMultipleApiBinding.inflate(layoutInflater)

    override fun FragmentMultipleApiBinding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentMultipleApiBinding.addEvents(savedInstanceState: Bundle?) {
        callAPI("")
    }

    override fun FragmentMultipleApiBinding.callAPI(
        apiKey: String,
        param: Any?,
        function: ((resultData: Any?) -> Unit)?
    ) {
        var result = ""
        viewModel.getPokemons().observe(this@MultipleApiFragment) {
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