package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.huyhieu.library.custom_views.UTab
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), View.OnClickListener {

    lateinit var mBinding: T

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun initializeBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
     */
    abstract fun initializeBinding(): T
    abstract fun addControls(savedInstanceState1: Bundle?)
    abstract fun addEvents(savedInstanceState1: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*Initialize binding*/
        mBinding = initializeBinding()
        setContentView(mBinding.root)
        handleEventOnBackPressed()
        setDarkColorStatusBar(isDark = true)
        window?.decorView?.rootView?.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        getDataFormIntent()
        addControls(savedInstanceState)
        addEvents(savedInstanceState)
        onLiveData()
    }

    private fun handleEventOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedActivity()
            }
        })
    }

    open fun getDataFormIntent() {

    }

    open fun onLiveData() {}

    /**
     * @apiKey : Key of API
     * */
    open fun callAPI(
        apiKey: String,
        param: Any? = null,
        function: ((resultData: Any?) -> Unit)? = null
    ) {
    }

    open fun setTabNavigationBottom(tab: UTab) {}

    open fun showNavigationBottom() {}

    open fun hideNavigationBottom(@ColorRes idColor: Int = android.R.color.transparent) {
        setNavigationBarColor(idColor)
    }

    open fun onBackPressedActivity() {
        onBackPressed()
    }
}