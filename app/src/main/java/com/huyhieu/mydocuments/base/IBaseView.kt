package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.huyhieu.mydocuments.base.interfaces.ILiveData
import java.lang.reflect.ParameterizedType

interface IBaseView<VB : ViewBinding> : View.OnClickListener, ILiveData<VB> {
    @Suppress("UNCHECKED_CAST")
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

    @Suppress("UNCHECKED_CAST")
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
        @Suppress("UNCHECKED_CAST") return method.invoke(null, inflater, container, false) as VB
    }

    /************************ Set onClick **************************/
    fun VB.setViewsClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this@IBaseView)
        }
    }

    fun VB.onClickViewBinding(v: View, id: Int) {}

    /************************ Navigate **************************/
    fun childNavigate(
        navHostFragment: NavHostFragment,
        directions: NavDirections,
        navOptionBuilder: (NavOptionsBuilder.() -> Unit)? = null
    ) {
        val navOpt = navOptionBuilder?.let { navOptions { it() } }
        navHostFragment.navController.navigate(directions, navOpt)
    }
}