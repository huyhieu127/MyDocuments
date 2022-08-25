package com.huyhieu.mydocuments.utils.commons

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.huyhieu.mydocuments.databinding.ViewMakerBinding
import com.huyhieu.mydocuments.utils.extensions.drawable

class UMarkerView constructor(context: Context) : ConstraintLayout(context) {
    private var markerBinding = ViewMakerBinding.inflate(LayoutInflater.from(context), this, true)


    fun setIcon(@DrawableRes drawableRes: Int) {
        markerBinding.imgLogoMarker.setImageDrawable(context.drawable(drawableRes))
    }
}