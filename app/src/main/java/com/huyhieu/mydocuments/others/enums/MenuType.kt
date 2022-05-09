package com.huyhieu.mydocuments.others.enums

sealed class MenuType {
    object MultipleAPI : MenuType()
    object Bluetooth : MenuType()
    object Notification : MenuType()
    object Steps : MenuType()
    object More : MenuType()
}