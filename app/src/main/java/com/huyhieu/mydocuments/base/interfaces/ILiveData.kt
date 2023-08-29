package com.huyhieu.mydocuments.base.interfaces

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

interface ILiveData<VB : ViewBinding> : IParams<VB>{

    fun onMyLiveData(savedInstanceState: Bundle?) {}

    /**
     * LiveData
     * */
    fun <Obj> LiveData<Obj>.observe(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner) { obj -> observer?.invoke(obj) }
    }

    fun <Obj> LiveData<Obj>.observeAndRemove(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(value: Obj?) {
                observer?.invoke(value)
                removeObserver(this)
            }
        })
    }

    fun <Obj> LiveData<Obj?>.observeNoneNull(observer: (Observer<Obj?>.(obj: Obj) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(value: Obj?) {
                value ?: return
                observer?.invoke(this, value)
            }
        })
    }

    fun <Obj> LiveData<Obj?>.observeNoneNullRemove(observer: (Observer<Obj?>.(obj: Obj) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(value: Obj?) {
                value ?: return
                observer?.invoke(this, value)
                removeObserver(this)
            }
        })
    }

    /**
     * MutableLiveData*/
    fun <Obj> MutableLiveData<Obj>.observeAndClean(observer: ((obj: Obj?) -> Unit)? = null) {
        this.observe(lifecycleOwner, object : Observer<Obj?> {
            override fun onChanged(value: Obj?) {
                value ?: return
                observer?.invoke(value)
                clear()
            }

        })
    }

    fun <Obj> MutableLiveData<Obj>.clear() {
        this.value = null
    }
}