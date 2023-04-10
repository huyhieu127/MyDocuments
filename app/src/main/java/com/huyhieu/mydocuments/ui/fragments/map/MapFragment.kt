package com.huyhieu.mydocuments.ui.fragments.map

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.huyhieu.library.extensions.isGooglePlayServicesAvailable
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.utils.MapUtils
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMapBinding


class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private var mapUtils: MapUtils? = null

    override fun FragmentMapBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(true)
        if (mActivity.isGooglePlayServicesAvailable()) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MapFragment)
            mapUtils = MapUtils(this@MapFragment)
        } else {
            map.isVisible = false
            imgLogo.isVisible = true
            tvMessage.isVisible = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapUtils?.bindGoogleMap(googleMap)
        mapUtils?.addNewMarker(LatLng(10.802702, 106.647505))
        mapUtils?.addNewMarker(LatLng(10.802702, 106.680000))
        mapUtils?.addNewMarker(LatLng(10.850002, 106.660000))
    }
}

