package com.huyhieu.mydocuments.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuForm(
    var type: String,
    var name: String,
    var note: String = "", var elements: List<MenuForm> = listOf(),
    var isAvailable: Boolean = true,
    var isExpanded: Boolean = false,
) : Parcelable

object HomeMenu {
    //Home menu
    const val MENU_ABOUT = "MENU_ABOUT"
    const val MENU_WIDGET = "MENU_WIDGET"
    const val MENU_SYSTEM = "MENU_SYSTEM"
    const val MENU_DIALOG = "MENU_DIALOG"
    const val MENU_OTHERS = "MENU_OTHERS"
}

object ChartMenu {
    const val MENU_RADAR_CHART = "MENU_RADAR_CHART"
    const val MENU_CUBIC_CHART = "MENU_CUBIC_CHART"
    const val MENU_LINE_CHART = "MENU_LINE_CHART"
}

object UIUXMenu {
    const val MENU_PULL_TO_REFRESH = "MENU_PULL_TO_REFRESH"
    const val MENU_PIN_CODE = "MENU_PIN_CODE"
    const val MENU_GUIDE = "MENU_GUIDE"
    const val MENU_SWIPE_RCV = "MENU_SWIPE_RCV"
    const val MENU_STEP_PAGE = "MENU_STEP_PAGE"
    const val MENU_BANNER_INFINITE = "MENU_BANNER_INFINITE"
    const val MENU_INTRODUCE = "MENU_INTRODUCE"
}

object FirebaseMenu {
    const val MENU_CLOUD_MESSAGING = "MENU_CLOUD_MESSAGING"
}


object ApiMenu {
    const val MENU_SDK = "MENU_SDK"
    const val MENU_MULTIPLE_NETWORK_API = "MENU_MULTIPLE_NETWORK_API"
}

object FeatureMenu {
    const val MENU_BROADCAST_POWER = "MENU_BROADCAST_POWER"
    const val MENU_BROADCAST_VOLUME = "MENU_BROADCAST_VOLUME"
    const val MENU_MAP = "MENU_MAP"
    const val MENU_STATIC_MAP = "MENU_STATIC_MAP"
    const val MENU_CONTENT_PROVIDER = "MENU_CONTENT_PROVIDER"
}

object SystemMenu {
    const val MENU_THEME = "MENU_THEME"
    const val MENU_LANGUAGE = "MENU_LANGUAGE"
}

object GoogleFeatureMenu {
    const val MENU_UPDATE_IN_APP = "MENU_UPDATE_IN_APP"
    const val MENU_DYNAMIC_FEATURES_FRAGMENT = "MENU_DYNAMIC_FEATURES_FRAGMENT"
    const val MENU_DYNAMIC_FEATURES_INCLUDE_GRAPH = "MENU_DYNAMIC_FEATURES_INCLUDE_GRAPH"
}
