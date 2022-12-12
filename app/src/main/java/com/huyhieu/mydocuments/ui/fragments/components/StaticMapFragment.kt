package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.bumptech.glide.Glide
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentStaticMapBinding

//Enable google map static in gg cloud
class StaticMapFragment : BaseFragmentOld<FragmentStaticMapBinding>() {
    private var locationUrl =
        "https://maps.googleapis.com/maps/api/staticmap" +
                "?center={shop_address}" +
                "&zoom=17" +
                "&size=700x200" +
                "&maptype=terrain" +
                "&format=png" +
                "&style=feature:poi|element:labels|visibility:off" +
                "&key=API_KEY"

    override fun initializeBinding() = FragmentStaticMapBinding.inflate(layoutInflater)

    override fun FragmentStaticMapBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()
    }

    override fun FragmentStaticMapBinding.addEvents(savedInstanceState: Bundle?) {
        val url = getStaticMapURL("10.802702, 106.647505")

        Glide.with(mActivity!!).load(url).into(mBinding.imgStaticMap)
    }

    private fun getStaticMapURL(latLng: String): String {
        val zoomVale = 16
        val size = "600x400"
        val mapType = "terrain"
        val marker1 = "color:red%7Clabel:G%7C$latLng"
        val style = "feature:poi|element:labels|visibility:off"
        //val key = BuildConfig.MAPS_API_KEY
        val key = "AIzaSyDelPRVm65-exFyFcziCHFS-YufW32T1Mo"

        return StringBuilder("https://maps.googleapis.com/maps/api/staticmap?center=")
            .append(latLng)
            .append("&zoom=$zoomVale")
            .append("&size=$size")
            .append("&maptype=$mapType")
            .append("&markers=$marker1")
            .append("&style=$style")
            .append("&key=$key").toString()
    }
}