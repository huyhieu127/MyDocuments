package com.huyhieu.mydocuments.ui.fragments.dialog.toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG_TOAST_SUCCESS = "TAG_TOAST_SUCCESS"
private var jobToastSuccess: Job? = null
fun Fragment.showToastSuccess(
    title: String = "Tiêu đề",
    content: String = "Nội dung",
    isCancelable: Boolean = true,
    onClose: ((ToastDialog) -> Unit)? = null
) {
    val toastAvailable = childFragmentManager.findFragmentByTag(TAG_TOAST_SUCCESS) as? ToastDialog

    if (toastAvailable != null) {
        toastAvailable.updateData(title = title, content = content)
        jobToastSuccess?.cancel()
        if (isCancelable) {
            jobToastSuccess = lifecycleScope.launch {
                delay(2000)
                toastAvailable.dismiss()
            }
        }
    } else {
        ToastDialog(type = ToastType.SUCCESS, title = title, content = content, onClose = onClose).apply {
            show(this@showToastSuccess.childFragmentManager, TAG_TOAST_SUCCESS)
            if (isCancelable) {
                jobToastSuccess = lifecycleScope.launch {
                    delay(2000)
                    dismiss()
                }
            }
        }
    }
}