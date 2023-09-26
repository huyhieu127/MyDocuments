package com.huyhieu.mydocuments.libraries.utils.map

import android.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.huyhieu.mydocuments.ui.fragments.map.MapManager


/**Measure Location*/
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

    return if (distance > MapManager.MAX_DISTANCE_SAVE_MARKER) {
        onValid?.invoke()
    } else {
        onIllegal?.invoke()
    }
}

fun CameraPosition.createLocation() = Location("locationCameraPosition").apply {
    latitude = target.latitude
    longitude = target.longitude
}

fun LatLng.toText(): String {
    return "${latitude},${longitude}"
}