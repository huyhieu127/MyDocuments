package com.huyhieu.mydocuments.ui.fragments.map

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragmentVM
import com.huyhieu.mydocuments.databinding.FragmentMapBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.isGooglePlayServicesAvailable
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.utils.map.MapCameraUtils
import com.huyhieu.mydocuments.libraries.utils.map.PolylineUtils
import com.huyhieu.mydocuments.utils.tryCatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapFragment : BaseFragmentVM<FragmentMapBinding, MapVM>(), OnMapReadyCallback,
    MapManagerCallback {

    private var mapManager: MapManager? = null

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(true)
        initView()
        setClickViews(vb.btnUserLocation, vb.btnLetsBegin)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapManager?.apply {
            bindGoogleMap(googleMap)
            addNewMarker(LatLng(10.802702, 106.647505))
            addNewMarker(LatLng(10.802702, 106.680000))
            addNewMarker(LatLng(10.850002, 106.660000))
        }
    }

    private fun initView() = with(vb) {
        if (mActivity.isGooglePlayServicesAvailable()) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MapFragment)
            mapManager = MapManager(this@MapFragment).apply {
                mapManagerCallback = this@MapFragment
            }
        } else {
            map.isVisible = false
            imgLogo.isVisible = true
            tvMessage.isVisible = true
        }
    }


    override fun onMyLiveData(savedInstanceState: Bundle?) {
        vm.directions.observe {
            it ?: return@observe
            tryCatch {
                val listPoints = PolylineUtils.decodePoints(it)
                mapManager?.apply {
                    latLngBounds = LatLngBounds.Builder()
                    userPosition?.let { latLng -> latLngBounds.include(latLng) }
                    polyline = PolylineUtils.drawPolyline(
                        googleMap = mapManager?.googleMap,
                        listLatLng = listPoints,
                        color = context.color(R.color.colorPrimary),
                        onEachLatLng = { latLng ->
                            latLngBounds.include(latLng)
                        },
                        onComeToEnd = {
                            mapManager?.polyline?.remove()
                        }
                    )
                }
            }
        }
    }

    override fun FragmentMapBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnUserLocation -> {
                if (mapManager?.destPosition != null) {
                    mapManager?.moveCameraToPolyline()
                } else {
                    mapManager?.userPosition?.let {
                        MapCameraUtils.cameraToLatLngAnimate(mapManager?.googleMap, it)
                    }
                }
            }

            btnLetsBegin -> {
                mapManager?.activeLocationUpdate()
                vb.btnLetsBegin.isVisible = false
            }
        }
    }

    override fun onUserLocationAvailable(location: Location) {
        super.onUserLocationAvailable(location)
        vb.btnUserLocation.isVisible = true
    }

    override fun onLocationUpdate(location: Location) {
        super.onLocationUpdate(location)
        val latLng = LatLng(location.latitude, location.longitude)
    }

    override fun onMapClicked(latLng: LatLng) {
        drawPolyline(latLng)
        mapManager?.latLngBoundsBetweenTwoPoints()
        mapManager?.moveCameraToPolyline()
    }

    override fun onMarkerClicker(marker: Marker) {
        super.onMarkerClicker(marker)
        val latLng = LatLng(marker.position.latitude, marker.position.longitude)
        drawPolyline(latLng)
        mapManager?.latLngBoundsBetweenTwoPoints()
        mapManager?.moveCameraToPolyline()
    }

    override fun onMarkerDragEnd(marker: Marker) {
        super.onMarkerDragEnd(marker)
        lifecycleScope.launch {
            //delay(MapManager.DELAY_CAMERA_IDLE)
            val latLng = LatLng(marker.position.latitude, marker.position.longitude)
            drawPolyline(latLng)
            mapManager?.latLngBoundsBetweenTwoPoints()
            mapManager?.moveCameraToPolyline()
        }
    }

    private fun drawPolyline(latLng: LatLng, isRemovePolylineNow: Boolean = true) {
        mapManager?.apply {
            if (isRemovePolylineNow) polyline?.remove()
            setCustomerMarker(latLng)
            vm.getDirections(originPosition, destPosition)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapManager?.releaseLocationUpdate()
    }
}

