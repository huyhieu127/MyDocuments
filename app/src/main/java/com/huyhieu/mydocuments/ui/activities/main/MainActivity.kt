package com.huyhieu.mydocuments.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI.StatusPokeAPI.*
import com.huyhieu.mydocuments.ui.activities.notification.NotificationActivity
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var viewModel: MainViewModel

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        initListMenu()
    }

    private fun initListMenu() {
        mBinding.rcvMenu.apply {
            setHasFixedSize(true)
            adapter = viewModel.adapterMenu
        }
        viewModel.adapterMenu.apply {
            fillData(viewModel.lstMenus)
            itemClick = { type ->
                when (type) {
                    MenuType.LoadMore -> {
                        showToastShort("LoadMore")
                    }
                    MenuType.SignInSocial -> {
                        showToastShort("SignInSocial")
                    }
                    MenuType.Notification -> {
                        startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
                    }
                    MenuType.More -> {
                        showToastShort("More")
                        MyDialog.getInstance().show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        callAPI()
    }

    override fun onClick(p0: View?) {
    }

    private fun callAPI() {
        val name = "API"
        viewModel.getPokemons().observe(this@MainActivity) {
            when (it.statusPokeAPI) {
                LOADING -> {
                    logDebug("$name - LOADING")
                    mBinding.progressBar.isVisible = true
                }
                COMPLETE -> {
                    logDebug("$name - COMPLETE")
                    mBinding.progressBar.isVisible = false
                    mBinding.rcvMenu.isVisible = true
                }
                NETWORK -> logDebug("$name - NETWORK")
                ERROR -> logDebug("$name - ERROR: ${it.message}")
                SUCCESS -> logDebug(
                    "$name<${it.index}> - SUCCESS: ${
                        it.response?.results?.get(
                            0
                        )?.name
                    }, size: ${it.response?.results?.size}"
                )
            }
        }
    }
}