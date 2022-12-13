package com.huyhieu.mydocuments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

interface IBaseView<VB : ViewBinding> : View.OnClickListener {

    @Suppress("UNCHECKED_CAST")
    fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return method.invoke(null, inflater, container, false) as VB
    }

    fun VB.handleViewClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this@IBaseView)
        }
    }

    fun VB.handleLiveData(savedInstanceState: Bundle?) {}

    fun VB.callAPI(apiKey: String, param: Any? = null, function: ((resultData: Any?) -> Unit)? = null) {}

    /************************ Navigate **************************/
    fun childNavigate(navHostFragment: NavHostFragment, directions: NavDirections, navOptionBuilder: (NavOptionsBuilder.() -> Unit)? = null) {
        val navOpt = navOptionBuilder?.let { navOptions { it() } }
        navHostFragment.navController.navigate(directions, navOpt)
    }
}