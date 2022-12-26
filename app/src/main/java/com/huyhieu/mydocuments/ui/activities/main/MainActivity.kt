package com.huyhieu.mydocuments.ui.activities.main

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setTransparentStatusBar
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.shared.appFireStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var mainVM: MainVM
    var storageRef = appFireStore.reference

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        //setFullScreen()
        setDarkColorStatusBar(false)
        setTransparentStatusBar(true)
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
       /* val jsonRef = storageRef.child("jsons/profile.json")
        val str: String = ("{\"name\":\"Hieu\", \"bod\": 1997 }")
        val uploadTask = jsonRef.putBytes(str.toByteArray())
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            logDebug(it.message)
        }.addOnProgressListener {
            logDebug("Progress: ${it.bytesTransferred}/${it.totalByteCount}")
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.\
            jsonRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { i ->
                val json = JSONObject(String(i))
                logDebug(json.toString())
            }.addOnFailureListener { i ->
                logDebug(i.message)
            }
        }*/

    }


    override fun onLiveData() {
        mainVM.pushNotify.observe(this) {
        }
    }

    override fun onClick(p0: View?) {
    }
}