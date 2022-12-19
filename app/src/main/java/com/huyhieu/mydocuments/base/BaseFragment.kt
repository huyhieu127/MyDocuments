package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.huyhieu.library.custom_views.MyButtonView
import com.huyhieu.library.extensions.hideKeyboard

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseView<VB> {
    private var _vb: VB? = null
    val vb: VB get() = _vb ?: throw NullPointerException("VB: ViewBinding has not been added yet!")

    val mActivity: BaseActivity<*> by lazy {
        try {
            activity
        } catch (ex: Exception) {
            requireActivity()
        } as BaseActivity<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _vb = getViewBinding(layoutInflater, container, savedInstanceState)
        val view = _vb?.root
        initViewParent(view)
        return view
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.onMyViewCreated(view, savedInstanceState)
        vb.handleLiveData(savedInstanceState)
    }

    private fun initViewParent(rootView: View?) {
        mActivity.hideKeyboard()
    }

    abstract fun VB.onMyViewCreated(view: View, savedInstanceState: Bundle?)

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }

    /************************ Handle click **************************/
    private val currentTime: Long get() = System.currentTimeMillis()
    private var time = 0L
    private val delayClick = 500L

    final override fun onClick(v: View?) {
        v ?: return
        if (currentTime - time > delayClick) {
            time = currentTime
            if (v is MyButtonView) {
                v.showLoading()
            }
            vb.onClickViewBinding(v, v.id)
        }
    }

    open fun VB.onClickViewBinding(v: View, id: Int) {}
}