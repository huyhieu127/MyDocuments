package com.huyhieu.mydocuments.ui.activities.main

import androidx.lifecycle.ViewModel
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import javax.inject.Inject

data class MainViewModel @Inject constructor(
    private val mapper: LiveDataMapper,
    private val apiService: APIService
) : ViewModel() {
    var offset = 0
    val adapterMenu by lazy { MenuAdapter() }
    var lstMenus = mutableListOf(
        MenuForm(name = "Load more items", type = MenuType.LoadMore),
        MenuForm(name = "Sign in with social", type = MenuType.SignInSocial),
        MenuForm(name = "Notification Firebase", type = MenuType.Notification),
        MenuForm(name = "More", type = MenuType.More)
    )

    /**Call multiple API*/
    fun getPokemons() =
        mapper.mapToLiveDataPokeAPI(
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                apiService.getPokemons(params)
            },
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                apiService.getPokemons(params)
            },
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                apiService.getPokemons(params)
            })
}
