package com.huyhieu.mydocuments.ui.fragments.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

interface MapManagerCallback {
    fun onUserLocationAvailable(location: Location) {}
    fun onLocationUpdate(location: Location) {}
    fun onMapClicked(latLng: LatLng) {}
    fun onMarkerClicker(marker: Marker) {}
    fun onMarkerDrag(marker: Marker) {}

    fun onMarkerDragEnd(marker: Marker) {}

    fun onMarkerDragStart(marker: Marker) {}
}