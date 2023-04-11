package com.huyhieu.mydocuments.base.interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.base.BaseActivity

interface IParams<VB: ViewBinding> {
    val vb: VB
    val mActivity: BaseActivity<*>
    val lifecycleOwner: LifecycleOwner
}