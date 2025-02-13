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
import com.google.maps.android.PolyUtil
import com.huyhieu.data.logger.logDebug
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragmentVM
import com.huyhieu.mydocuments.databinding.FragmentMapBinding
import com.huyhieu.mydocuments.libraries.extensions.color
import com.huyhieu.mydocuments.libraries.extensions.isGooglePlayServicesAvailable
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.utils.map.MapCameraUtils
import com.huyhieu.mydocuments.libraries.utils.map.PolylineHelpers
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
                val sys = System.currentTimeMillis()
                //v\al listPoints = PolylineHelpers.decodePoints(it)
                val listPoints = latLngList
                // Độ chính xác (meter), giảm số lượng điểm
                val tolerance = 50.0
                val simplifiedPoints = PolyUtil.simplify(listPoints, tolerance)
                mapManager?.apply {
                    latLngBounds = LatLngBounds.Builder()
                    userPosition?.let { latLng -> latLngBounds.include(latLng) }
                    polyline = PolylineHelpers.drawPolyline(
                        googleMap = mapManager?.googleMap,
                        listLatLng = simplifiedPoints,
                        color = context.color(R.color.colorPrimary),
                        onEachLatLng = { latLng ->
                            latLngBounds.include(latLng)
                        },
                        onComeToEnd = {
                            mapManager?.polyline?.remove()
                        }
                    )
                }
                logDebug("Time: ${System.currentTimeMillis() - sys}")
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

val latLngList = mutableListOf(
    LatLng(10.817971, 106.627060),
    LatLng(10.815627, 106.628189),
    LatLng(10.812931, 106.629601),
    LatLng(10.810565, 106.631226),
    LatLng(10.809789, 106.631592),
    LatLng(10.806948, 106.632248),
    LatLng(10.804709, 106.632698),
    LatLng(10.802679, 106.633224),
    LatLng(10.799248, 106.634163),
    LatLng(10.797459, 106.634819),
    LatLng(10.795348, 106.635742),
    LatLng(10.792211, 106.636993),
    LatLng(10.789649, 106.637939),
    LatLng(10.787376, 106.638878),
    LatLng(10.784669, 106.640244),
    LatLng(10.781988, 106.641663),
    LatLng(10.779876, 106.642647),
    LatLng(10.776541, 106.644165),
    LatLng(10.774245, 106.645233),
    LatLng(10.770861, 106.646576),
    LatLng(10.768130, 106.647736),
    LatLng(10.765420, 106.648888),
    LatLng(10.763058, 106.649940),
    LatLng(10.760291, 106.651115),
    LatLng(10.756618, 106.652702),
    LatLng(10.753330, 106.654129),
    LatLng(10.749611, 106.655563),
    LatLng(10.746497, 106.656784),
    LatLng(10.743681, 106.657913),
    LatLng(10.740769, 106.659050),
//    LatLng(10.738300, 106.660126),
//    LatLng(10.736453, 106.661026),
//    LatLng(10.735455, 106.661774),
//    LatLng(10.733495, 106.663017),
//    LatLng(10.731210, 106.664398),
//    LatLng(10.729433, 106.665314),
//    LatLng(10.728103, 106.666000),
//    LatLng(10.726732, 106.666740),
//    LatLng(10.724478, 106.668129),
//    LatLng(10.723499, 106.668693),
//    LatLng(10.722620, 106.669151),
//    LatLng(10.721340, 106.669769),
//    LatLng(10.720059, 106.670349),
//    LatLng(10.718068, 106.671287),
//    LatLng(10.717050, 106.671723),
//    LatLng(10.715946, 106.672203),
//    LatLng(10.713703, 106.673187),
//    LatLng(10.712254, 106.673950),
//    LatLng(10.710847, 106.674606),
//    LatLng(10.709715, 106.675186),
//    LatLng(10.708294, 106.675827),
//    LatLng(10.707043, 106.676422),
//    LatLng(10.705753, 106.677040),
//    LatLng(10.704546, 106.677643),
//    LatLng(10.703475, 106.678162),
//    LatLng(10.702152, 106.678734),
//    LatLng(10.700813, 106.679329),
//    LatLng(10.699607, 106.679871),
//    LatLng(10.698462, 106.680427),
//    LatLng(10.696873, 106.681152),
//    LatLng(10.695169, 106.681946),
//    LatLng(10.693438, 106.682800),
//    LatLng(10.691835, 106.683495),
//    LatLng(10.690169, 106.684311),
//    LatLng(10.688317, 106.685165),
//    LatLng(10.687044, 106.685760),
//    LatLng(10.685235, 106.686577),
//    LatLng(10.683581, 106.687347),
//    LatLng(10.682029, 106.688072),
//    LatLng(10.680415, 106.688751),
//    LatLng(10.679144, 106.689301),
//    LatLng(10.677387, 106.690041),
//    LatLng(10.675852, 106.690628),
//    LatLng(10.674139, 106.691300),
//    LatLng(10.673058, 106.691741),
//    LatLng(10.671349, 106.692440),
//    LatLng(10.669722, 106.693123),
//    LatLng(10.668101, 106.693794),
//    LatLng(10.666667, 106.694405),
//    LatLng(10.665208, 106.694984),
//    LatLng(10.663756, 106.695549),
//    LatLng(10.662345, 106.696068),
//    LatLng(10.660931, 106.696571),
//    LatLng(10.659539, 106.697075),
//    LatLng(10.658177, 106.697563),
//    LatLng(10.656839, 106.698044),
//    LatLng(10.655501, 106.698502),
//    LatLng(10.654183, 106.698982),
//    LatLng(10.652932, 106.699432),
//    LatLng(10.651723, 106.699875),
//    LatLng(10.650515, 106.700295),
//    LatLng(10.649290, 106.700714),
//    LatLng(10.648064, 106.701164),
//    LatLng(10.646789, 106.701599),
//    LatLng(10.645468, 106.702049),
//    LatLng(10.644151, 106.702461),
//    LatLng(10.642944, 106.702850),
//    LatLng(10.641725, 106.703247),
//    LatLng(10.640544, 106.703621),
//    LatLng(10.639371, 106.703995),
//    LatLng(10.638196, 106.704361),
//    LatLng(10.637036, 106.704735),
//    LatLng(10.635897, 106.705094),
//    LatLng(10.634694, 106.705482),
//    LatLng(10.633556, 106.705849),
//    LatLng(10.632345, 106.706200),
//    LatLng(10.631087, 106.706543),
//    LatLng(10.630004, 106.706871),
//    LatLng(10.628830, 106.707214),
//    LatLng(10.627677, 106.707550),
//    LatLng(10.626557, 106.707870),
//    LatLng(10.625396, 106.708183),
//    LatLng(10.624297, 106.708488),
//    LatLng(10.623113, 106.708786),
//    LatLng(10.622007, 106.709091),
//    LatLng(10.620897, 106.709381),
)
