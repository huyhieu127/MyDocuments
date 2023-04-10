package com.huyhieu.mydocuments.navigation.directions

import com.huyhieu.mydocuments.ui.fragments.navigation.QRCodeFragmentDirections
import com.huyhieu.mydocuments.ui.fragments.navigation.home.HomeFragmentDirections


object HomeDirections {
    val toHome get() = HomeFragmentDirections.actionGlobalHomeFragment()

    val toQR get() = QRCodeFragmentDirections.actionGlobalQrCodeFragment()

    val toProfile get() = QRCodeFragmentDirections.actionGlobalProfileFragment()
}