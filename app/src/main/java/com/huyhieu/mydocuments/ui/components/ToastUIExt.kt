package com.huyhieu.mydocuments.ui.components

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG_TOAST_SUCCESS = "TAG_TOAST_SUCCESS"
private var jobToastSuccess: Job? = null
private var count = 0
fun Fragment.showToastSuccess(title: String = "Tiêu đề", content: String = "Nội dung") {
    val toastAvailable = childFragmentManager.findFragmentByTag(TAG_TOAST_SUCCESS) as? ToastDialog

    if (toastAvailable != null) {
        jobToastSuccess?.cancel()
        toastAvailable.updateData(title = title, content = content)
        jobToastSuccess = lifecycleScope.launch {
            delay(2000)
            toastAvailable.dismiss()
        }

    } else {
        ToastDialog(type = ToastType.SUCCESS, title = title, content = content).apply {
            show(this@showToastSuccess.childFragmentManager, TAG_TOAST_SUCCESS)
            jobToastSuccess = lifecycleScope.launch {
                delay(2000)
                dismiss()
            }
        }
    }
}