package com.huyhieu.mydocuments.others.enums

sealed class MenuType {
    object LoadMore : MenuType()
    object SignInSocial : MenuType()
    object Notification : MenuType()
    object More : MenuType()
}