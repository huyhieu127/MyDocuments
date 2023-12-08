package com.huyhieu.mydocuments.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuForm(
    var type: String,
    var name: String,
    var note: String = "",
    var isAvailable: Boolean = true,

    var onNavigate: (() -> Unit)? = null
) : Parcelable

object HomeMenu {
    const val MENU_DYNAMIC_FEATURES_FRAGMENT = "MENU_DYNAMIC_FEATURES_FRAGMENT"
    const val MENU_DYNAMIC_FEATURES_INCLUDE_GRAPH = "MENU_DYNAMIC_FEATURES_INCLUDE_GRAPH"
    const val MENU_UPDATE_IN_APP = "MENU_UPDATE_IN_APP"

    //Home menu
    const val MENU_ABOUT = "MENU_ABOUT"
    const val MENU_THEME = "MENU_THEME"
    const val MENU_LANGUAGE = "MENU_LANGUAGE"
    const val MENU_NETWORK_API = "MENU_NETWORK_API"
    const val MENU_WIDGET = "MENU_WIDGET"
    const val MENU_CHART = "MENU_CHART"
    const val MENU_NOTIFICATION = "MENU_NOTIFICATION"
    const val MENU_SYSTEM = "MENU_SYSTEM"
    const val MENU_DIALOG = "MENU_DIALOG"
    const val MENU_OTHERS = "MENU_OTHERS"
}