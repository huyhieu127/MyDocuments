package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentProfileBinding
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun initializeBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun FragmentProfileBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()
        showNavigationBottom()
    }

    override fun FragmentProfileBinding.addEvents(savedInstanceState: Bundle?) {
        optInformation.setOnClickListener(this@ProfileFragment)
    }

    override fun FragmentProfileBinding.onClickViewBinding(v: View) {
        when (v.id) {
            optInformation.id -> {
                mActivity.showToastShort("Thông tin cá nhân")
            }
        }
    }
}