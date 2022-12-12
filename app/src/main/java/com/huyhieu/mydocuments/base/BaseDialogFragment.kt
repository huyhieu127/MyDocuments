package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.huyhieu.library.custom_views.UButtonView
import com.huyhieu.library.extensions.hideKeyboard
import com.huyhieu.mydocuments.R

abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment(), View.OnClickListener {

    val mBinding: T by lazy { initializeBinding() }

    val mActivity: BaseActivity<*>? by lazy {
        try {
            activity as BaseActivity<*>
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * - Force assignment of value after class has been initialized.
     * + Example:
     *   override fun initializeBinding(inflater: LayoutInflater,container: ViewGroup? ) = ResultProfileBinding.inflate(layoutInflater)
     */
    abstract fun initializeBinding(): T

    abstract fun T.onReady(savedInstanceState: Bundle?)

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
        mBinding.onReady(savedInstanceState)
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

    /**
     * Config dialog*/

    override fun getTheme(): Int {
        return R.style.CustomAlertDialog
    }

    fun getWindow() = this.dialog?.window

    fun setTopDialog() {
        getWindow()?.let { window ->
            val wlp: WindowManager.LayoutParams = window.attributes
            wlp.gravity = Gravity.TOP
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
        }
    }

    fun setTouchBehindDialog() {
        getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
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
}