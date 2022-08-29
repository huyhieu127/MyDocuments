package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.utils.commons.UButtonView
import com.huyhieu.mydocuments.utils.commons.UTab
import com.huyhieu.mydocuments.utils.extensions.hideKeyboard

abstract class BaseFragment<T : ViewBinding> : Fragment(), View.OnClickListener {

    val mBinding: T by lazy { initializeBinding() }

    val mActivity: BaseActivity<*>? by lazy {
        try {
            activity as BaseActivity<*>
        }catch (ex: Exception){
            null
        }
    }

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun initializeBinding(inflater: LayoutInflater,container: ViewGroup? ) = ResultProfileBinding.inflate(layoutInflater)
     */
    abstract fun initializeBinding(): T

    abstract fun T.addControls(savedInstanceState: Bundle?)
    abstract fun T.addEvents(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = mBinding.root
        initViewParent(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.addControls(savedInstanceState)
        mBinding.addEvents(savedInstanceState)
        mBinding.onLiveData(savedInstanceState)
    }

    private fun initViewParent(rootView: View?) {
        mActivity?.hideKeyboard()
        handleBackDevice(rootView)
    }

    open fun T.onLiveData(savedInstanceState: Bundle?) {}

    open fun T.callAPI(
        apiKey: String,
        param: Any? = null,
        function: ((resultData: Any?) -> Unit)? = null
    ) {
    }

    /************************ Navigate **************************/
    fun childNavigate(
        navHostFragment: NavHostFragment,
        directions: NavDirections,
        navOptionBuilder: (NavOptionsBuilder.() -> Unit)? = null
    ) {
        val navOpt = navOptionBuilder?.let { navOptions { it() } }
        navHostFragment.navController.navigate(directions, navOpt)
    }

    /************************ Handle click **************************/
    private val currentTime: Long
        get() = System.currentTimeMillis()
    private var time = 0L
    private val delayClick = 1000L

    final override fun onClick(v: View?) {
        v ?: return
        if (currentTime - time > delayClick) {
            time = currentTime
            if (v is UButtonView) {
                v.showLoading()
            }
            mBinding.onClickViewBinding(v)
        }
    }

    open fun T.onClickViewBinding(v: View) {

    }

    /******************* Handle keyboard back device *******************/
    private fun handleBackDevice(rootView: View?) {
        rootView?.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        onBackPressedFragment()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }


    open fun onBackPressedFragment() {
        mActivity?.onBackPressed()
    }

    open fun setTabNavigationBottom(tab: UTab) {
        mActivity?.setTabNavigationBottom(tab)
    }

    open fun showNavigationBottom() {
        mActivity?.showNavigationBottom()
    }

    open fun hideNavigationBottom(@ColorRes idColor: Int = android.R.color.transparent) {
        mActivity?.hideNavigationBottom(idColor)
    }
}