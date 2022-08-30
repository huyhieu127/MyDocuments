package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentProfileBinding
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.toHyperText

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

        val text =
            "Bằng việc đăng ký tài khoản, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi."
        val terms = "Điều khoản sử dụng"
        val policy = "Chính sách bảo mật"
        val spTermAndPolicy = text.toHyperText(terms) {
            //Do something
            context.showToastShort("Term: ${ckb.isChecked}")
        }.toHyperText(policy) {
            //Do something
            context.showToastShort("Policy: ${ckb.isChecked}")
        }
        ckb.setTextContent(spTermAndPolicy)
        ckb.checkBoxListener = {
            context.showToastShort("State: $it")
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