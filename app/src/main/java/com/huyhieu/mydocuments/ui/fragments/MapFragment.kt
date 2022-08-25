package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMapBinding
import com.huyhieu.mydocuments.utils.commons.MapUtils
import com.huyhieu.mydocuments.utils.extensions.isGooglePlayServicesAvailable
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar


class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private var mapUtils: MapUtils? = null

    override fun initializeBinding() = FragmentMapBinding.inflate(layoutInflater)

    override fun FragmentMapBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar(true)

        if (mActivity?.isGooglePlayServicesAvailable() == true) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MapFragment)
            mapUtils = MapUtils(this@MapFragment)
        } else {
            map.isVisible = false
            imgLogo.isVisible = true
            tvMessage.isVisible = true
        }
    }

    override fun FragmentMapBinding.addEvents(savedInstanceState: Bundle?) {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapUtils?.bindGoogleMap(googleMap)
    }
}

