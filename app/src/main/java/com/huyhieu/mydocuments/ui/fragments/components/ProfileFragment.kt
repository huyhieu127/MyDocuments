package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.library.extensions.toSpannable
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentProfileBinding
import com.huyhieu.mydocuments.ui.components.ToastDialog
import com.huyhieu.mydocuments.ui.components.ToastType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun FragmentProfileBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
        //showNavigationBottom()
        //hideNavigationBottom(R.color.white)
        handleViewClick(optInformation, noteSelfSetupLoan, noteSelfSetupLoan.tvSeeMore)

        val text =
            "Bằng việc đăng ký tài khoản, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi."
        val terms = "Điều khoản sử dụng"
        val policy = "Chính sách bảo mật"
        val spTermAndPolicy = text.toSpannable(requireContext(), terms) {
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
        }.toSpannable(requireContext(), policy) {
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

    override fun FragmentProfileBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
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