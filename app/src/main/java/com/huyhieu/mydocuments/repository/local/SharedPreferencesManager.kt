package com.huyhieu.mydocuments.repository.local

import android.content.Context

class SharedPreferencesManager(private val context: Context?) {

    companion object {
        private const val MY_SHARED_PREF = "MY_SHARED_PREF"
        private const val URL_BASE_DEFAULT_DEBUG = "URL_BASE_DEFAULT_DEBUG"
        private const val IMAGE_URL = "IMAGE_URL"
        private const val USER_INFO = "USER_INFO"
        private const val IS_REMEMBER = "IS_REMEMBER"
    }

    var isRememberLogin: Boolean
        get() {
            val sharedPref = context?.getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE) ?: return false
            return sharedPref.getBoolean(IS_REMEMBER, false)
        }
        set(value) {
            val sharedPref = context?.getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putBoolean(IS_REMEMBER, value)
                apply()
            }
        }

}