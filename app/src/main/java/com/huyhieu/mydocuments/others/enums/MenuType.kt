package com.huyhieu.mydocuments.others.enums

sealed class MenuType {
    object Guide : MenuType()
    object MotionCard : MenuType()
    object FFmpegKit : MenuType()
    object MultipleAPI : MenuType()
    object Bluetooth : MenuType()
    object Notification : MenuType()
    object Steps : MenuType()
    object More : MenuType()
}