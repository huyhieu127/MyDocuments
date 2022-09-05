package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentProfileBinding
import com.huyhieu.mydocuments.utils.dialog.ToastDialog
import com.huyhieu.mydocuments.utils.dialog.ToastType
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.toHyperText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            context.showToastShort("Term: ${ckb.isChecked}")
            ToastDialog(
                type = ToastType.SUCCESS,
                title = "Cài lại mật khẩu thành công",
                content = "Bạn có thể đăng nhập bằng mật khẩu mới",
            ).apply {
                lifecycleScope.launch {
                    show(this@ProfileFragment.childFragmentManager, "TOAST")
                    delay(2000)
                    dismiss()
                }
            }
        }.toHyperText(policy) {
            context.showToastShort("Policy: ${ckb.isChecked}")
            ToastDialog(
                type = ToastType.FAILED,
                title = "Đã có lỗi xảy ra",
                content = "Bạn vui lòng thử lại lần nữa",
            ).apply {
                lifecycleScope.launch {
                    show(this@ProfileFragment.childFragmentManager, "TOAST")
                    delay(2000)
                    dismiss()
                }
            }
        }
        ckb.setTextContent(spTermAndPolicy)
        ckb.checkBoxListener = {
            ToastDialog(
                type = ToastType.NORMAL,
                title = "Chào mừng bạn đến với UBank",
                content = "Bắt đầu trải nghiệm những điều kỳ diệu trong ngân hàng số dành riêng cho bạn.",
            ).apply {
                show(this@ProfileFragment.childFragmentManager, "TOAST")
                lifecycleScope.launch {
                    delay(2000)
                    dismiss()
                }
            }
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