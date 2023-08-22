package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.huyhieu.library.custom_views.MyButtonView
import com.huyhieu.mydocuments.base.interfaces.IBaseView
import com.huyhieu.mydocuments.ui.activities.main.MainVM
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.HomeVM
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseView<VB> {

    @Inject
    lateinit var mainVM: MainVM

    @Inject
    lateinit var homeVM: HomeVM

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

    override val lifecycleOwner: LifecycleOwner get() = this.viewLifecycleOwner

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _vb = getViewBinding(layoutInflater, container, savedInstanceState)
        val view = _vb?.root
        vb.setupCreateView()
        return view
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onMyViewCreated(savedInstanceState)
        onMyLiveData(savedInstanceState)
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
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