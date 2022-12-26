package com.huyhieu.mydocuments.shared

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context?) {

    /**
     * Config SharedPreferencesManager
     * */
    private val mySharedPref get() = context?.getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE)

    private fun SharedPreferences?.setValue(onInsertValue: SharedPreferences.Editor.() -> Unit) {
        this ?: return
        with(this.edit()) {
            onInsertValue()
            apply()
        }
    }

    /**
     * Constants of SharedPreferences
     * */
    companion object {
        private const val MY_SHARED_PREF = "MY_SHARED_PREF"

        private const val IS_LOADED_INTRODUCE = "IS_LOADED_INTRODUCE"
    }

    /**
     * Get|Set data of SharedPreferences
     * */
    var isLoadedIntroduce: Boolean
        get() {
            return mySharedPref?.getBoolean(IS_LOADED_INTRODUCE, false) ?: false
        }
        set(value) {
            mySharedPref?.setValue {
                putBoolean(IS_LOADED_INTRODUCE, value)
            }
        }

}