package com.huyhieu.mydocuments.base

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.base.interfaces.IBaseView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseFragmentVM<VB : ViewBinding, VM : ViewModel> :
    BaseFragment<VB>(),
    IBaseView<VB> {

    @Inject
    lateinit var vm: VM
}