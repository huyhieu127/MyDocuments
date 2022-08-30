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
        noteSelfSetupLoan.setOnClickListener(this@ProfileFragment)
        noteSelfSetupLoan.tvSeeMore.setOnClickListener(this@ProfileFragment)

        ckb.checkBoxListener = {
            context.showToastShort("$it ~ ${ckb.isChecked}")
        }
    }

    override fun FragmentProfileBinding.onClickViewBinding(v: View) {
        when (v.id) {
            optInformation.id -> {
                mActivity.showToastShort("Thông tin cá nhân")
            }
            noteSelfSetupLoan.id -> {
                mActivity.showToastShort("Khoản vay tự thiết lập")
            }
            noteSelfSetupLoan.tvSeeMore.id -> {
                mActivity.showToastShort("Tìm hiểu thêm")
            }
        }
    }
}