package com.huyhieu.mydocuments.ui.fragments.navigation.home

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.base.dialog.BaseBottomSheetDialogFragment
import com.huyhieu.mydocuments.databinding.FragmentPopupNotifyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopupNotifyFragment : BaseBottomSheetDialogFragment<FragmentPopupNotifyBinding>() {
    companion object {
        fun newInstance(): PopupNotifyFragment {
            return PopupNotifyFragment()
        }
    }

    /*override fun getTheme(): Int {
        return R.style.CustomBottomSheetModal
    }*/

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        initView()
        setViewsClick(vb.btnClose)
    }

    private fun FragmentPopupNotifyBinding.initView() {
        tvAuthor.text = "huyhieu127"
        tvEmail.text = "id.huyhieu@gmail.com"
        tvVersionName.text = BuildConfig.VERSION_NAME
        tvVersionCode.text = BuildConfig.VERSION_CODE.toString()
        tvBuildType.text = BuildConfig.BUILD_TYPE.uppercase()
    }

    override fun FragmentPopupNotifyBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            vb.btnClose.id -> {
                dismissAllowingStateLoss()
            }
        }
    }
}