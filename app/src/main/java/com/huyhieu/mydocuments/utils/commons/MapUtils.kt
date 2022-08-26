package com.huyhieu.mydocuments.utils.commons

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.ui.IconGenerator
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.utils.extensions.drawable
import com.huyhieu.mydocuments.utils.extensions.isGps
import com.huyhieu.mydocuments.utils.logDebug
import com.huyhieu.mydocuments.utils.requestActivityResult
import com.huyhieu.mydocuments.utils.requestPermissions
import kotlinx.coroutines.*

class MapUtils(private val fragment: Fragment) {

    companion object {
        const val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

        //Config
        const val DELAY_CAMERA_IDLE = 800L
        const val MAX_DISTANCE_SAVE_MARKER = 1000L
        const val CAMERA_ZOOM = 16F
    }

    private lateinit var googleMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var locationDest: Location? = null
    private lateinit var context: Context
    private var latLngBounds = LatLngBounds.Builder()

    private val requestPermission = fragment.requestPermissions(
        onGranted = {
            getCurrentLocation()
        },
        onDined = {
            logDebug("Dined! - ACCESS_FINE_LOCATION")
        }
    )

    private val requestGps = fragment.requestActivityResult {
        getCurrentLocation()
    }


    init {
        try {
            context = fragment.requireContext()
            requestPermission.launch(
                arrayOf(
                    PERMISSION_ACCESS_FINE_LOCATION,
                    PERMISSION_ACCESS_COARSE_LOCATION
                )
            )
        } catch (ex: Exception) {
        }
    }

    @SuppressLint("MissingPermission")
    fun bindGoogleMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setScrollMap()
        setMarkerClick()
    }

    private fun setScrollMap(onCameraStop: ((cameraPosition: CameraPosition) -> Unit)? = null) {
        googleMap.apply {
            var jobGetCameraPositionIdle: Job? = null

            this.setOnCameraIdleListener {
                val cameraPosition = this.cameraPosition
                jobGetCameraPositionIdle = fragment.lifecycleScope.launch {
                    delay(DELAY_CAMERA_IDLE)
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
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setMarkerClick() {
        googleMap.apply {
            setOnMarkerClickListener { marker ->
                logDebug("Title: ${marker.title}\nTag: ${marker.tag}\nLatLng: ${marker.position.latitude} ~ ${marker.position.longitude}")
                val latLng = LatLng(marker.position.latitude, marker.position.longitude)
                cameraToLatLngAnimate(latLng)
                false
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (!context.isGps()) {
            AlertDialog.Builder(context).apply {
                setMessage("GPS is turn off, go to setting!")
                setPositiveButton("OK") { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    requestGps.launch(intent)
                }
                setOnCancelListener {
                    (fragment as BaseFragment<*>).onBackPressedFragment()
                }
            }.show()

        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null)
                    logDebug("lastLocation not found!")
                else {
                    lastLocation = location
                    val lat = location.latitude
                    val lng = location.longitude
                    val latLng = LatLng(lat, lng)
                    cameraToLatLng(latLng)
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
                        addNewMarker(
                            latLng,
                            tag = "current",
                            title = "Your location",
                            icon = R.drawable.img_android_logo
                        )
//                        location.calculateDistanceValid(locationDest = lastLocation) {
//                            cameraToLatLng(latLng)
//                        }
                        //cameraToLatLngAnimate(latLng)
                        cameraLatLngBoundsAnimate()
                    }

                }
        }
    }

    private fun cameraToLatLngAnimate(latLng: LatLng, zoom: Float = CAMERA_ZOOM) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun cameraLatLngBoundsAnimate(padding: Int = 100) {
        try {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), padding)
            )
        } catch (ex: Exception) {

        }
    }

    private fun cameraToLatLng(latLng: LatLng, zoom: Float = CAMERA_ZOOM) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun addNewMarker(
        latLng: LatLng,
        @DrawableRes icon: Int = R.drawable.img_google_play,
        tag: String? = null,
        title: String? = null
    ) {
        fragment.lifecycleScope.launch {
            val markerIcon = getMarkerIcon(icon)
            val markerOptions = async(Dispatchers.IO) {
                MarkerOptions()
                    .position(latLng)
                    .icon(markerIcon)
                    .title(title)
                    .anchor(0.5F, 0.85F)
            }
            val marker = googleMap.addMarker(markerOptions.await())
            marker?.tag = tag
            latLngBounds.include(latLng)
        }
    }

    private fun getMarkerIcon(@DrawableRes drawableRes: Int): BitmapDescriptor? {
        val markerView = UMarkerView(context)
        markerView.setIcon(drawableRes)
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

        return if (distance > MAX_DISTANCE_SAVE_MARKER) {
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