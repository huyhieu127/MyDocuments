package com.huyhieu.mydocuments.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.ui.IconGenerator
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMapBinding
import com.huyhieu.mydocuments.utils.UMarkerView
import com.huyhieu.mydocuments.utils.extensions.drawable
import com.huyhieu.mydocuments.utils.extensions.isGps
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.logDebug
import com.huyhieu.mydocuments.utils.requestActivityResult
import com.huyhieu.mydocuments.utils.requestPermissions
import kotlinx.coroutines.*


class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var locationDest: Location? = null

    private val requestPermission = requestPermissions(
        onGranted = {
            getCurrentLocation()
        },
        onDined = {
            logDebug("Dined! - ACCESS_FINE_LOCATION")
        }
    )

    private val requestGps = requestActivityResult {
        getCurrentLocation()
    }

    override fun initializeBinding() = FragmentMapBinding.inflate(layoutInflater)

    override fun FragmentMapBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar(true)

        if (mActivity?.isGooglePlayServicesAvailable() == true) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MapFragment)
            requestPermission.launch(permissions)
        } else {
            map.isVisible = false
            imgLogo.isVisible = true
            tvMessage.isVisible = true
        }
    }

    override fun FragmentMapBinding.addEvents(savedInstanceState: Bundle?) {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setScrollMap()
        this.googleMap.setMarkerClick()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (!mActivity.isGps()) {
            AlertDialog.Builder(mActivity).apply {
                setMessage("GPS is turn off, go to setting!")
                setPositiveButton("OK") { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    requestGps.launch(intent)
                }
                setOnCancelListener {
                    onBackPressedFragment()
                }
            }.show()

        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity!!)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null)
                    logDebug("lastLocation not found!")
                else {
                    lastLocation = location
                    val lat = location.latitude
                    val lng = location.longitude
                    val latLng = LatLng(lat, lng)
                    googleMap?.cameraToLatLng(latLng)
                }
            }
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        logDebug("getCurrentLocation not found!")
                    else {
                        val lat = location.latitude
                        val lng = location.longitude
                        val latLng = LatLng(lat, lng)
                        googleMap.addNewMarker(latLng, tag = "current", title = "Your location")
                        location.calculateDistanceValid(locationDest = lastLocation) {
                            googleMap?.cameraToLatLng(latLng)
                        }
                        googleMap.cameraToLatLngAnimate(latLng)
                    }

                }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun GoogleMap?.setMarkerClick() {
        this ?: return
        this.setOnMarkerClickListener { marker ->
            mActivity.showToastShort("Title: ${marker.title}\nTag: ${marker.tag}\nLatLng: ${marker.position.latitude} ~ ${marker.position.longitude}")
            val latLng = LatLng(marker.position.latitude, marker.position.longitude)
            googleMap.cameraToLatLngAnimate(latLng)
            true
        }
    }

    private fun GoogleMap?.setScrollMap(onCameraStop: ((cameraPosition: CameraPosition) -> Unit)? = null) {
        this ?: return
        var jobGetCameraPositionIdle: Job? = null
        this.setOnCameraIdleListener {
            val cameraPosition = this.cameraPosition
            jobGetCameraPositionIdle = lifecycleScope.launch {
                delay(800)
                val lat = cameraPosition.target.latitude
                val lng = cameraPosition.target.longitude
                val locationCameraPosition = createLocation("locationCameraPosition", lat, lng)

                //Init location dest
                if (locationDest == null) {
                    locationDest = locationCameraPosition
                    onCameraStop?.invoke(cameraPosition)
                    logDebug("Distance FIRST: $cameraPosition")
                }
                //Calculate distance between two location
                locationCameraPosition.calculateDistanceValid(locationDest,
                    onResult = {
                        logDebug("Distance: $it")
                    },
                    onValid = {
                        locationDest = locationCameraPosition
                        onCameraStop?.invoke(cameraPosition)
                        logDebug("Distance OK: $cameraPosition")
                    }
                )
            }
        }
        this.setOnCameraMoveListener {
            jobGetCameraPositionIdle?.cancel()
        }
    }

    private fun GoogleMap?.cameraToLatLngAnimate(latLng: LatLng, zoom: Float = 16F) {
        this ?: return
        this.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun GoogleMap?.cameraToLatLng(latLng: LatLng, zoom: Float = 16F) {
        this ?: return
        this.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun GoogleMap?.addNewMarker(
        latLng: LatLng,
        tag: String? = null,
        title: String? = null
    ) {
        this ?: return
        lifecycleScope.launch {
            val markerOptions = async(Dispatchers.IO) {
                MarkerOptions()
                    .position(latLng)
                    .icon(getMarkerIcon(mActivity!!))
                    .title(title)
                    .anchor(0.5F, 0.85F)
            }
            val marker = this@addNewMarker.addMarker(markerOptions.await())
            marker?.tag = tag
        }
    }

    private fun Activity?.isGooglePlayServicesAvailable(): Boolean {
        this ?: return false
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            /*if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404)!!.show()
            }*/
            return false
        }
        return true
    }

    fun getMarkerIcon(context: Context): BitmapDescriptor? {
        val markerView = UMarkerView(context)
        markerView.setIcon(R.drawable.ic_baseline_file_copy_24)
        val iconGenerator = IconGenerator(context)
        iconGenerator.apply {
            setBackground(context.drawable(android.R.color.transparent))
            setContentView(markerView)
        }
        val bitmap = iconGenerator.makeIcon()
        //val bitmap = markerView.toBitmap()
        return if (bitmap != null) {
            BitmapDescriptorFactory.fromBitmap(bitmap)
        } else {
            null
        }
    }

    fun Location?.calculateDistanceValid(
        locationDest: Location?,
        onResult: ((distance: Float) -> Unit)? = null,
        onIllegal: (() -> Unit)? = null,
        onValid: (() -> Unit)? = null
    ): Unit? {
        this ?: return onIllegal?.invoke()
        locationDest ?: return onIllegal?.invoke()

        val distance = this.distanceTo(locationDest)
        onResult?.invoke(distance)

        return if (distance > 1000F) {
            onValid?.invoke()
        } else {
            onIllegal?.invoke()
        }
    }

    fun createLocation(provider: String, lat: Double, lng: Double) =
        Location(provider).apply {
            latitude = lat
            longitude = lng
        }
}

