package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.libraries.custom_views.UTab
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), View.OnClickListener {

    lateinit var vb: VB

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun binding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
     */
    abstract fun binding(): VB
    abstract fun addControls(savedInstanceState1: Bundle?)
    abstract fun addEvents(savedInstanceState1: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*Initialize binding*/
        vb = binding()
        setContentView(vb.root)
        setDarkColorStatusBar(isDark = true)
        window?.decorView?.rootView?.setBackgroundColor(color(R.color.white))
        addControls(savedInstanceState)
        addEvents(savedInstanceState)
        onMyLiveData()
    }

    open fun onMyLiveData() {}

    open fun setTabNavigationBottom(tab: UTab) {}

    open fun showNavigationBottom() {}

    open fun hideNavigationBottom(@ColorRes idColor: Int = android.R.color.transparent) {
        setNavigationBarColor(idColor)
    }
}