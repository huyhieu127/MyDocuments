package com.huyhieu.mydocuments.ui.activities.multipleapi

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMultipleApiactivityBinding
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MultipleAPIActivity : BaseActivity<ActivityMultipleApiactivityBinding>() {
    @Inject
    lateinit var viewModel: MultipleAPIViewModel

    override fun initializeBinding() = ActivityMultipleApiactivityBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        callAPI("")
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
    }

    override fun onClick(p0: View?) {
    }

    override fun callAPI(apiKey: String, param: Any?, function: ((resultData: Any?) -> Unit)?) {
        var result = ""
        viewModel.getPokemons().observe(this) {
            when (it.statusPokeAPI) {
                ResultPokeAPI.StatusPokeAPI.LOADING -> {
                    logDebug(" - LOADING")
                    mBinding.progressBar.isVisible = true
                }
                ResultPokeAPI.StatusPokeAPI.COMPLETE -> {
                    logDebug(" - COMPLETE")
                    mBinding.progressBar.isVisible = false
                }
                ResultPokeAPI.StatusPokeAPI.NETWORK -> logDebug(" - NETWORK")
                ResultPokeAPI.StatusPokeAPI.ERROR -> logDebug(" - ERROR: ${it.message}")
                ResultPokeAPI.StatusPokeAPI.SUCCESS -> {
                    val response =
                        "<${it.index}> - SUCCESS: ${it.response?.results?.first()?.name}, size: ${it.response?.results?.size}"
                    logDebug(response)
                    result = (result + "\n" + response)
                    mBinding.tvResult.text = result
                }
            }
        }
    }
}