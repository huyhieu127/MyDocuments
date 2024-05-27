package com.huyhieu.mydocuments.ui.activities.main

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.extensions.setTransparentStatusBar
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.shared.appFireStore
import com.huyhieu.mydocuments.utils.helper.PendingIntentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var mainVM: MainVM

//    @Inject
//    lateinit var testVM: TesVM

    var storageRef = appFireStore.reference

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        backupNotification()
    }

    override fun binding() = ActivityMainBinding.inflate(layoutInflater)

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

        //testVM.insertUser(User(1, "Hieu", "Mail"))
        //testVM.getUser(1)
    }

    private fun backupNotification() {
        intent.extras?.let { extras ->
            val type = extras.getString("type").orEmpty()
            val data = extras.getString("promotionId").orEmpty()
            if (type.isNotEmpty() && type != "0") {
                logDebug("backupNotification: $type, data: $data")
                PendingIntentFactory.direction(this, type, data).send()
            }
        }
    }

    override fun onMyLiveData() {
        mainVM.pushNotify.observe(this) {
        }

//        testVM.user.observe(this) {
//            logDebug("user: $it")
//        }
    }

    override fun onClick(p0: View?) {
    }
}