package com.huyhieu.mydocuments.ui.fragments.steps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStepsBinding
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.ui.activities.bluetooth.BluetoothActivity
import com.huyhieu.mydocuments.ui.activities.ffmmpegkit.FFmpegKitActivity
import com.huyhieu.mydocuments.ui.activities.main.MyDialog
import com.huyhieu.mydocuments.ui.activities.multipleapi.MultipleAPIActivity
import com.huyhieu.mydocuments.ui.activities.notification.NotificationActivity
import com.huyhieu.mydocuments.ui.activities.steps.StepsActivity

class StepsFragment : BaseFragment<FragmentStepsBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStepsBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}