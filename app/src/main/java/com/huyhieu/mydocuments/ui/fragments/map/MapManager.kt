package com.huyhieu.mydocuments.ui.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.ui.IconGenerator
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.libraries.custom_views.MyMarkerSimpleView
import com.huyhieu.mydocuments.libraries.custom_views.MyMarkerView
import com.huyhieu.mydocuments.libraries.extensions.drawable
import com.huyhieu.mydocuments.libraries.extensions.isGps
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.libraries.utils.logError
import com.huyhieu.mydocuments.libraries.utils.map.MapCameraUtils
import com.huyhieu.mydocuments.libraries.utils.map.calculateDistanceValid
import com.huyhieu.mydocuments.libraries.utils.map.createLocation
import com.huyhieu.mydocuments.libraries.utils.requestActivityResult
import com.huyhieu.mydocuments.libraries.utils.requestPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MapManager(private val fragment: Fragment) {

    companion object {
        const val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

        //Config
        const val DELAY_CAMERA_IDLE = 800L
        const val MAX_DISTANCE_SAVE_MARKER = 1000L
    }

    var googleMap: GoogleMap? = null
    var mapManagerCallback: MapManagerCallback? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    var userLocation: Location? = null
    var latestLocation: Location? = null
    var cameraLocation: Location? = null

    val userPosition: LatLng? get() = userLocation?.let { return LatLng(it.latitude, it.longitude) }
    var originPosition: LatLng? = null
        get() {
            return field ?: userPosition
        }
    var destPosition: LatLng? = null


    private lateinit var context: Context
    private lateinit var defaultLifecycleObserver: DefaultLifecycleObserver

    var polyline: Polyline? = null

    private var markerUser: Marker? = null
    private var markerCustomer: Marker? = null
    private var markerVehicleMarker: Marker? = null

    var latLngBounds = LatLngBounds.Builder()

    private val requestPermission = fragment.requestPermissions(onGranted = {
        handleGetLocation()
    }, onDined = {
        logDebug("Dined! - ACCESS_FINE_LOCATION")
    })

    private val requestGps = fragment.requestActivityResult {
        handleGetLocation()
    }


    init {
        try {
            context = fragment.requireContext()
            requestPermission.launch(
                arrayOf(
                    PERMISSION_ACCESS_FINE_LOCATION, PERMISSION_ACCESS_COARSE_LOCATION
                )
            )
            setupLocation()
        } catch (ex: Exception) {
            logError(ex.printStackTrace().toString().ifEmpty { ex.message })
        }
    }

    /**Update UI*/
    @SuppressLint("MissingPermission")
    fun bindGoogleMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap?.configMap()
        setMapClick()
        setMarkerClick()
        setMarkerDrag()
        setScrollMap()
    }

    private fun GoogleMap?.configMap() {
        this ?: return
        //mapType = GoogleMap.MAP_TYPE_TERRAIN
        // Customise the styling of the base map using a JSON object defined in a raw resource file.
        val isSuccess = setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        if (!isSuccess) logDebug("Style parsing failed.")
    }

    private fun setMapClick() {
        googleMap?.setOnMapClickListener {
            mapManagerCallback?.onMapClicked(it)
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setMarkerClick() {
        googleMap?.apply {
            setOnMarkerClickListener { marker ->
                val latLng = LatLng(marker.position.latitude, marker.position.longitude)
                MapCameraUtils.cameraToLatLngAnimate(googleMap, latLng)
                mapManagerCallback?.onMarkerClicker(marker)
                true//false to use direction of Google Maps App
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setMarkerDrag() {
        googleMap?.apply {
            setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
                override fun onMarkerDrag(marker: Marker) {
                    mapManagerCallback?.onMarkerDrag(marker)
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    mapManagerCallback?.onMarkerDragEnd(marker)
                }

                override fun onMarkerDragStart(marker: Marker) {
                    mapManagerCallback?.onMarkerDragStart(marker)
                }

            })
        }
    }

    private fun setScrollMap(onCameraStop: ((cameraPosition: CameraPosition) -> Unit)? = null) {
        googleMap?.apply {
            var jobGetCameraPositionIdle: Job? = null

            this.setOnCameraIdleListener {
                val cameraPosition = this.cameraPosition
                jobGetCameraPositionIdle = fragment.lifecycleScope.launch {
                    delay(DELAY_CAMERA_IDLE)
                    val locationCameraPosition = cameraPosition.createLocation()
                    //Init location dest
                    if (cameraLocation == null) {
                        cameraLocation = locationCameraPosition
                        onCameraStop?.invoke(cameraPosition)
                        logDebug("Distance FIRST: $cameraPosition")
                    }
                    //Calculate distance between two location
                    locationCameraPosition.calculateDistanceValid(cameraLocation, onResult = {
                        logDebug("Distance: $it")
                    }, onValid = {
                        cameraLocation = locationCameraPosition
                        onCameraStop?.invoke(cameraPosition)
                        logDebug("Distance OK: $cameraPosition")
                    })
                }
            }
            this.setOnCameraMoveListener {
                jobGetCameraPositionIdle?.cancel()
            }
        }
    }

    private fun setMarker(
        latLng: LatLng,
        @DrawableRes icon: Int = R.drawable.img_google_play,
        tag: String? = null,
        title: String? = null
    ): Marker? {
        val markerIcon = simpleMarker(icon)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(markerIcon)
            .title(title)
        val marker = googleMap?.addMarker(markerOptions)
        marker?.tag = tag
        return marker
    }


    fun addNewMarker(
        latLng: LatLng,
        @DrawableRes icon: Int = R.drawable.img_google_play,
        tag: String? = null,
        title: String? = null
    ) {
        fragment.lifecycleScope.launch {
            val markerIcon = customMarker(icon)
            val markerOptions = async(Dispatchers.IO) {
                MarkerOptions().position(latLng).icon(markerIcon).title(title).anchor(0.5F, 0.85F)
                    .rotation(0.25F)
            }
            val marker = googleMap?.addMarker(markerOptions.await())
            marker?.tag = tag
            latLngBounds.include(latLng)
        }
    }

    /**
     * Types marker
     * */
    private fun setUserMarker(location: Location) {
        if (userLocation == null) {
            userLocation = location
            val latLng = LatLng(location.latitude, location.longitude)
            markerUser = setMarker(latLng, icon = R.drawable.ic_user_location)
            MapCameraUtils.cameraToLatLngZoom(googleMap, latLng)
        }
    }

    fun setCustomerMarker(latLng: LatLng) {
        markerCustomer?.remove()
        destPosition = latLng
        markerCustomer = setMarker(latLng, icon = R.drawable.ic_customer_location)
        markerCustomer?.isDraggable = true
        moveCameraToPolyline()
    }

    fun moveCameraToPolyline() {
        latLngBounds = LatLngBounds.Builder()
        originPosition?.also { latLngBounds.include(it) }
        destPosition?.also { latLngBounds.include(it) }
        MapCameraUtils.cameraLatLngBoundsAnimate(googleMap, latLngBounds)
    }

    private fun setVehicleMarker(
        location: Location?,
        @DrawableRes icon: Int = R.drawable.ic_car_red,
        tag: String? = null,
        title: String? = null
    ) {
        location ?: return
        fragment.lifecycleScope.launch {
            val latLng = LatLng(location.latitude, location.longitude)
            // val markerIcon = getMarkerIcon(icon)
            val bitmap = BitmapDescriptorFactory.fromResource(icon)
            val markerOptions = async(Dispatchers.IO) {
                MarkerOptions().position(latLng).icon(bitmap).title(title).anchor(0.5F, 0.5F)
                    .rotation(location.bearing)
            }
            markerVehicleMarker?.remove()
            markerVehicleMarker = googleMap?.addMarker(markerOptions.await())
            markerVehicleMarker?.tag = tag
            //latLngBounds.include(latLng)
            MapCameraUtils.cameraToLatLng(googleMap, latLng = latLng, isAnimate = true)
        }
    }

    /**
     * Custom marker
     * */
    private fun simpleMarker(@DrawableRes drawableRes: Int): BitmapDescriptor? {
        val view = MyMarkerSimpleView(context)
        view.setIcon(drawableRes)
        return view.createBitmapDescriptor()
    }

    private fun customMarker(@DrawableRes drawableRes: Int): BitmapDescriptor? {
        val markerView = MyMarkerView(context)
        markerView.setIcon(drawableRes)
        return markerView.createBitmapDescriptor()
    }

    private fun View.createBitmapDescriptor(): BitmapDescriptor? {
        val iconGenerator = IconGenerator(context)
        iconGenerator.apply {
            setBackground(context.drawable(android.R.color.transparent))
            setContentView(this@createBitmapDescriptor)
        }
        val bitmap = iconGenerator.makeIcon()
        //val bitmap = markerView.toBitmap()
        return if (bitmap != null) {
            BitmapDescriptorFactory.fromBitmap(bitmap)
        } else {
            null
        }
    }

    /**Handle location*/
    private fun setupLocation() {
        // Define the accuracy based on your needs and granted permissions
        val priority = Priority.PRIORITY_HIGH_ACCURACY// Priority.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest = LocationRequest.Builder(priority, TimeUnit.SECONDS.toMillis(2)).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    latestLocation = location
                    setVehicleMarker(latestLocation)
                }
            }
        }
        defaultLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                startLocationUpdates()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                stopLocationUpdates()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                fragment.lifecycle.removeObserver(this)
            }
        }
    }

    fun activeLocationUpdate() {
        fragment.lifecycle.addObserver(defaultLifecycleObserver)
    }

    fun releaseLocationUpdate() {
        fragment.lifecycle.removeObserver(defaultLifecycleObserver)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun handleGetLocation(onCancel: ((DialogInterface) -> Unit)? = null) {
        if (!context.isGps()) {
            AlertDialog.Builder(context).apply {
                setMessage("GPS is turn off, go to setting!")
                setPositiveButton("OK") { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    requestGps.launch(intent)
                }
                setOnCancelListener {
                    //(fragment as BaseFragment<*>).onBackPressedFragment()
                    onCancel?.invoke(it)
                }
            }.show()

        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            /**
             * Get static location
             * */
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    setUserMarker(location)
                } else {
                    logDebug("lastLocation not found!")
                }
            }
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    setUserMarker(location)
////                        location.calculateDistanceValid(locationDest = lastLocation) {
////                            cameraToLatLng(latLng)
//                    // MapCameraUtils.cameraToLatLngAnimate(googleMap,latLng)
//                    MapCameraUtils.cameraToLatLngAnimate(googleMap)
                } else {
                    logDebug("getCurrentLocation not found!")
                }
            }
        }
    }
}