package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
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

    abstract fun addControls(savedInstanceState: Bundle?)
    abstract fun addEvents(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = mBinding.root
        initViewParent(view)
        return view
    }

    private fun initViewParent(rootView: View?) {
        mActivity?.hideKeyboard()
        handleBackDevice(rootView)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addControls(savedInstanceState)
        addEvents(savedInstanceState)
    }

    open fun callAPI(
        apiKey: String,
        param: Any? = null,
        function: ((resultData: Any?) -> Unit)? = null
    ) {
    }

    open fun onBackPressedFragment() {
        mActivity?.onBackPressed()
    }
}