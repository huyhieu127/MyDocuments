package com.huyhieu.mydocuments.base.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.libraries.extensions.hideKeyboard
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
interface IBaseView<VB : ViewBinding> : INavigationController<VB>, IViewClickListener<VB>,
    ILiveData<VB> {

    /**
     * Abstract function
     * */
    fun onMyViewCreated(savedInstanceState: Bundle?)

    /**
     * Function
     * */
    fun VB.setupCreateView() {
        mActivity.hideKeyboard()
        root.handleBackDevice()
    }

    fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): VB {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return method.invoke(null, inflater, container, false) as VB
    }

    fun getViewBinding2(
        inflater: LayoutInflater, container: ViewGroup? = null, savedInstanceState: Bundle?
    ): VB {
        var genericVBClass = javaClass
        var cls: Class<VB>? = null
        while (cls !is Class<VB>) {
            val type = genericVBClass.genericSuperclass as? ParameterizedType
            cls = type?.actualTypeArguments?.get(0) as? Class<VB>
            if (cls == null) {
                genericVBClass = genericVBClass.superclass as Class<IBaseView<VB>>
            }
        }
        val method = cls.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return method.invoke(null, inflater, container, false) as VB
    }
}