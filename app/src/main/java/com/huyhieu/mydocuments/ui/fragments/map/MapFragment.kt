package com.huyhieu.mydocuments.ui.fragments.map

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMapBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.isGooglePlayServicesAvailable
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.utils.map.PolylineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback,
    MapManagerCallback {

    @Inject
    lateinit var vm: MapVM

    private var mapManager: MapManager? = null

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(true)
        initView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapManager?.apply {
            bindGoogleMap(googleMap)
            addNewMarker(LatLng(10.802702, 106.647505))
            addNewMarker(LatLng(10.802702, 106.680000))
            addNewMarker(LatLng(10.850002, 106.660000))

            originPosition = LatLng(10.806757988465815, 106.63892042315102)
            destPosition = LatLng(10.7996, 106.641)
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
            val listPoints = PolylineUtils.decodePoints(it)
            mapManager?.polyline = PolylineUtils.drawPolyline(
                mapManager?.googleMap,
                listPoints,
                context.color(R.color.colorPrimary)
            )
        }
    }

    override fun onMapClicked(latLng: LatLng) {
        drawPolyline(latLng)
    }

    override fun onMarkerClicker(marker: Marker) {
        super.onMarkerClicker(marker)
        val latLng = LatLng(marker.position.latitude, marker.position.longitude)
        drawPolyline(latLng)
    }

    override fun onMarkerDragEnd(marker: Marker) {
        super.onMarkerDragEnd(marker)
        lifecycleScope.launch {
            //delay(MapManager.DELAY_CAMERA_IDLE)
            val latLng = LatLng(marker.position.latitude, marker.position.longitude)
            drawPolyline(latLng)
        }
    }

    private fun drawPolyline(latLng: LatLng) {
        mapManager?.apply {
            setCustomerMarker(latLng)
            moveCameraToPolyline()
            polyline?.remove()
            vm.getDirections(originPosition, destPosition)
        }
    }
}

