package com.huyhieu.mydocuments.ui.fragments.map

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStaticMapBinding
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.utils.map.toText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaticMapFragment : BaseFragment<FragmentStaticMapBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?): Unit = with(vb) {
        mActivity.setDarkColorStatusBar()
        val url = getStaticMapURL(
            latLng = LatLng(10.8068, 106.6388).toText(),
            style = style_dark_mode
        ).toString() //style_dark_mode
        Glide.with(mActivity).load(url).into(imgStaticMap)
    }

    private fun getStaticMapURL(latLng: String, style: String): StringBuilder {
        val key = BuildConfig.MAPS_API_KEY
        //val key = "AIzaSyDelPRVm65-exFyFcziCHFS-YufW32T1Mo"
        val marker1 = "color:red|label:G|$latLng"
        return StringBuilder("https://maps.googleapis.com/maps/api/staticmap?")
            .append("key=$key")
            .append("&center=$latLng")
            .append("&zoom=16")
            .append("&size=600x300")
            .append("&format=png")
            .append("&maptype=roadmap")
            .append(style)

            .append("&markers=$marker1")
        //.append("&markers=$marker2")

    }

    companion object {
        private const val style_light_mode =
            "&style=element:geometry%7Ccolor:0xf5f5f5&style=element:labels.icon%7Cvisibility:off&style=element:labels.text.fill%7Ccolor:0x616161&style=element:labels.text.stroke%7Ccolor:0xf5f5f5&style=feature:administrative.land_parcel%7Celement:labels.text.fill%7Ccolor:0xbdbdbd&style=feature:poi%7Celement:geometry%7Ccolor:0xeeeeee&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x757575&style=feature:poi.park%7Celement:geometry%7Ccolor:0xe5e5e5&style=feature:poi.park%7Celement:labels.text.fill%7Ccolor:0x9e9e9e&style=feature:road%7Celement:geometry%7Ccolor:0xffffff&style=feature:road.arterial%7Celement:labels.text.fill%7Ccolor:0x757575&style=feature:road.highway%7Celement:geometry%7Ccolor:0xdadada&style=feature:road.highway%7Celement:labels.text.fill%7Ccolor:0x616161&style=feature:road.local%7Celement:labels.text.fill%7Ccolor:0x9e9e9e&style=feature:transit.line%7Celement:geometry%7Ccolor:0xe5e5e5&style=feature:transit.station%7Celement:geometry%7Ccolor:0xeeeeee&style=feature:water%7Celement:geometry%7Ccolor:0xc9c9c9&style=feature:water%7Celement:labels.text.fill%7Ccolor:0x9e9e9e"
        private const val style_dark_mode =
            "&style=element:geometry%7Ccolor:0x212121&style=element:labels.icon%7Cvisibility:off&style=element:labels.text.fill%7Ccolor:0x757575&style=element:labels.text.stroke%7Ccolor:0x212121&style=feature:administrative%7Celement:geometry%7Ccolor:0x757575&style=feature:administrative.country%7Celement:labels.text.fill%7Ccolor:0x9e9e9e&style=feature:administrative.land_parcel%7Cvisibility:off&style=feature:administrative.locality%7Celement:labels.text.fill%7Ccolor:0xbdbdbd&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x757575&style=feature:poi.park%7Celement:geometry%7Ccolor:0x181818&style=feature:poi.park%7Celement:labels.text.fill%7Ccolor:0x616161&style=feature:poi.park%7Celement:labels.text.stroke%7Ccolor:0x1b1b1b&style=feature:road%7Celement:geometry.fill%7Ccolor:0x2c2c2c&style=feature:road%7Celement:labels.text.fill%7Ccolor:0x8a8a8a&style=feature:road.arterial%7Celement:geometry%7Ccolor:0x373737&style=feature:road.highway%7Celement:geometry%7Ccolor:0x3c3c3c&style=feature:road.highway.controlled_access%7Celement:geometry%7Ccolor:0x4e4e4e&style=feature:road.local%7Celement:labels.text.fill%7Ccolor:0x616161&style=feature:transit%7Celement:labels.text.fill%7Ccolor:0x757575&style=feature:water%7Celement:geometry%7Ccolor:0x000000&style=feature:water%7Celement:labels.text.fill%7Ccolor:0x3d3d3d"
    }
}