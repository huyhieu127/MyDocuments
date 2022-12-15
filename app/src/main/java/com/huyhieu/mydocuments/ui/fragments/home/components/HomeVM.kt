package com.huyhieu.mydocuments.ui.fragments.home.components

import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.others.enums.MenuType
import javax.inject.Inject

class HomeVM @Inject constructor() : BaseVM() {
    val adapterMenu by lazy { MenuAdapter() }
    var lstMenus = mutableListOf(
        MenuForm(name = "Motion Card", type = MenuType.MOTION),
        MenuForm(name = "Mobile FFmpeg Kit", type = MenuType.FFMPEG_KIT),
        MenuForm(name = "Call multiple API", type = MenuType.MULTIPLE_API),
        MenuForm(name = "Bluetooth", type = MenuType.BLUETOOTH),
        MenuForm(name = "Steps", type = MenuType.STEPS),
        MenuForm(name = "More", type = MenuType.COMPONENTS),
        //Fake
    )
}
