package com.huyhieu.mydocuments.utils.extensions

import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

fun Context?.isGps(): Boolean {
    this ?: return false
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    return manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
}

fun Context?.isGooglePlayServicesAvailable(): Boolean {
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

fun Context?.showToastShort(msg: String?) {
    if (this == null) return
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}