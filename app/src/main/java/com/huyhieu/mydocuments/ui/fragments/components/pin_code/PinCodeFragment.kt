package com.huyhieu.mydocuments.ui.fragments.components.pin_code

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentPinCodeBinding
import com.huyhieu.mydocuments.ui.fragments.dialog.toast.showToastSuccess


class PinCodeFragment : BaseFragment<FragmentPinCodeBinding>() {

    override fun FragmentPinCodeBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        //Create instance toast
        showToastSuccess(title = "Mã pin của bạn", content = "Mời bạn nhập mã pin", isCancelable = false) {
            it.updateData(content = "Mời bạn nhập mã pin")
            keyBoardNumber.clear()
        }
        keyBoardNumber.outputCallback = { pin ->
            //Reuse instance toast
            if (pin.isEmpty()) {
                showToastSuccess(title = "Mã pin của bạn", content = "Mời bạn nhập mã pin", isCancelable = false)
            } else {
                showToastSuccess(title = "Mã pin của bạn", content = pin, isCancelable = false)
            }
        }
    }
}