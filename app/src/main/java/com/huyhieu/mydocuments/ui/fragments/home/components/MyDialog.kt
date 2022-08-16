package com.huyhieu.mydocuments.ui.fragments.home.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.huyhieu.mydocuments.R

class MyDialog : DialogFragment() {

    companion object {
        private var instance: MyDialog? = null
        fun getInstance(): MyDialog {
            if (instance == null) {
                instance = MyDialog()
            }
            return instance as MyDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_dialog_fragment, container);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editTextTextPersonName)
        editText.setText("HELLO")
    }
}