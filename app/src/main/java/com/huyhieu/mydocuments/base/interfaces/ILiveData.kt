package com.huyhieu.mydocuments.base.interfaces

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

interface ILiveData<VB : ViewBinding> {
    val lifecycleOwner: LifecycleOwner

    fun VB.onMyLiveData(savedInstanceState: Bundle?) {}

    /**
     * LiveData
     * */
    fun <Obj> LiveData<Obj>.observe(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner) { obj -> observer?.invoke(obj) }
    }


    fun <Obj> LiveData<Obj>.observeAndRemove(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(obj: Obj?) {
                observer?.invoke(obj)
                removeObserver(this)
            }
        })
    }

    /**
     * MutableLiveData*/
    fun <Obj> MutableLiveData<Obj>.observeAndClean(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(obj: Obj?) {
                obj ?: return
                observer?.invoke(obj)
                clear()
            }

        })
    }

    fun <Obj> MutableLiveData<Obj>.clear() {
        this.value = null
    }
}