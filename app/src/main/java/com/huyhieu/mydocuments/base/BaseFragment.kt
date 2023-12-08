package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.base.interfaces.IBaseView
import com.huyhieu.mydocuments.libraries.custom_views.MyButtonView
import com.huyhieu.mydocuments.ui.activities.main.MainVM
import com.huyhieu.mydocuments.ui.fragments.navigation.home.components.HomeVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

    /**---------------- Lifecycle ----------------**/
    fun CoroutineScope.repeatOnLifecycle(
        state: Lifecycle.State = Lifecycle.State.CREATED,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launch(context, start) {
            lifecycle.repeatOnLifecycle(state) {
                block.invoke(this)
            }
        }
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

    fun setOnBackPressed(block: OnBackPressedCallback.() -> Unit) =
        mActivity.onBackPressedDispatcher.addCallback(lifecycleOwner) {
            block(this)
        }

}

