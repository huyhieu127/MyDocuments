package com.huyhieu.mydocuments.libraries.custom_views

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.huyhieu.mydocuments.databinding.ViewMakerSimpleBinding
import com.huyhieu.mydocuments.libraries.extensions.drawable

class MyMarkerSimpleView(context: Context) : ConstraintLayout(context) {
    private var markerBinding =
        ViewMakerSimpleBinding.inflate(LayoutInflater.from(context), this, true)


    fun setIcon(@DrawableRes drawableRes: Int) {
        markerBinding.root.setImageDrawable(context.drawable(drawableRes))
    }
}