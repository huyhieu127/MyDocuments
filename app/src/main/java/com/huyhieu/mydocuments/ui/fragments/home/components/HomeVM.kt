package com.huyhieu.mydocuments.ui.fragments.home.components

import androidx.lifecycle.ViewModel
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import javax.inject.Inject

data class HomeVM @Inject constructor(
    private val mapper: LiveDataMapper,
    private val apiService: APIService
) : ViewModel() {
    val adapterMenu by lazy { MenuAdapter() }
    var lstMenus = mutableListOf(
        MenuForm(name = "Motion Card", type = MenuType.MotionCard),
        MenuForm(name = "Mobile FFmpeg Kit", type = MenuType.FFmpegKit),
        MenuForm(name = "Call multiple API", type = MenuType.MultipleAPI),
        MenuForm(name = "Bluetooth", type = MenuType.Bluetooth),
        MenuForm(name = "Steps", type = MenuType.Steps),
        MenuForm(name = "More", type = MenuType.More)
    )
}
