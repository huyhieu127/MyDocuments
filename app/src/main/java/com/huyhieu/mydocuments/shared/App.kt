package com.huyhieu.mydocuments.shared

import com.huyhieu.mydocuments.App

/**
 * Get data from instance App
 * */
val appShared by lazy { App.ins.sharedPref }

val appFireStore by lazy { App.ins.fireStore }