package com.huyhieu.mydocuments.libraries.custom_views

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.huyhieu.mydocuments.databinding.ViewMakerBinding
import com.huyhieu.mydocuments.libraries.extensions.drawable

class MyMarkerView constructor(context: Context) : ConstraintLayout(context) {
    private var markerBinding = ViewMakerBinding.inflate(LayoutInflater.from(context), this, true)


    fun setIcon(@DrawableRes drawableRes: Int) {
        markerBinding.imgLogoMarker.setImageDrawable(context.drawable(drawableRes))
    }
}