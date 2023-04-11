package com.huyhieu.mydocuments.ui.fragments.navigation.home

import android.os.Bundle
import android.view.View
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

    override fun FragmentPopupNotifyBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        setAllowTouchBehind()
        dialog?.setCanceledOnTouchOutside(true)
        setViewsClick(vb.btnClose)
    }

    override fun FragmentPopupNotifyBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            vb.btnClose.id -> {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun FragmentPopupNotifyBinding.onMyLiveData(savedInstanceState: Bundle?) {
        homeVM.githubCommittedLiveData.observe {
            it ?: return@observe
            val lastedCommit = it.first()
            tvAuthor.text = lastedCommit.commit.committer.name
            tvEmail.text = lastedCommit.commit.committer.email
            tvMassage.text = lastedCommit.commit.message + "\n\n\nUpdate time: ${System.nanoTime()}"
        }
    }
}