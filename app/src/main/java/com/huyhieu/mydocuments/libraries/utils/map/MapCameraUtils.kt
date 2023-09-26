package com.huyhieu.mydocuments.libraries.utils.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object MapCameraUtils {
    const val CAMERA_ZOOM = 16F
    fun cameraToLatLngAnimate(googleMap: GoogleMap?, latLng: LatLng, zoom: Float = CAMERA_ZOOM) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun cameraLatLngBoundsAnimate(
        googleMap: GoogleMap?,
        latLngBounds: LatLngBounds.Builder,
        padding: Int = 300
    ) {
        try {
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), padding)
            )
        } catch (ex: Exception) {

        }
    }

    fun cameraToLatLngZoom(googleMap: GoogleMap?, latLng: LatLng, zoom: Float = CAMERA_ZOOM) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun cameraToLatLng(googleMap: GoogleMap?, latLng: LatLng, isAnimate: Boolean = true) {
        if (isAnimate) {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

}