package com.huyhieu.mydocuments.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huyhieu.library.custom_views.MyButtonView
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.base.interfaces.IBaseView
import com.huyhieu.mydocuments.ui.activities.main.MainVM
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.HomeVM
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment(),
    IBaseView<VB> {

    @Inject
    lateinit var mainVM: MainVM

    @Inject
    lateinit var homeVM: HomeVM

    private val window get() = this.dialog?.window

    private var _vb: VB? = null

    /**
     * Params interface
     * */
    override val vb: VB
        get() = _vb ?: throw NullPointerException("VB: ViewBinding has not been added yet!")

    override val mActivity: BaseActivity<*> by lazy {
        try {
            activity
        } catch (ex: Exception) {
            requireActivity()
        } as BaseActivity<*>
    }

    override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    /**
     * Functions
     * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _vb = getViewBinding(layoutInflater, container, savedInstanceState)
        val view = _vb?.root
        vb.setupCreateView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vb.onMyViewCreated(savedInstanceState)
        vb.onMyLiveData(savedInstanceState)
    }

    /**
     * Config dialog
     * */
    override fun getTheme(): Int {
        return R.style.CustomAlertDialog
    }

    fun setAllowTouchBehind() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
    }

    /**---------------- Handle click ----------------**/
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

}