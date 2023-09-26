package com.huyhieu.mydocuments.ui.fragments.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

interface MapManagerCallback {
    fun onMapClicked(latLng: LatLng) {}
    fun onMarkerClicker(marker: Marker) {}
    fun onMarkerDrag(marker: Marker) {}

    fun onMarkerDragEnd(marker: Marker) {}

    fun onMarkerDragStart(marker: Marker) {}
}