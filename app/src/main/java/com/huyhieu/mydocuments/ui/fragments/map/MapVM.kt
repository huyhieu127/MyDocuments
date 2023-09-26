package com.huyhieu.mydocuments.ui.fragments.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.utils.toJson
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class MapVM @Inject constructor() : BaseVM() {
    private val _directions = MutableLiveData<JSONObject>()
    val directions: LiveData<JSONObject> = _directions
    private var job: Job? = null

    fun getDirections(origin: LatLng?, dest: LatLng?) {
        if (origin == null || dest == null) return
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val parameters = mutableMapOf<String, Any>(
                    "origin" to  "${origin.latitude},${origin.longitude}",
                    "destination" to  "${dest.latitude},${dest.longitude}",
                    "sensor" to  false,
                    "mode" to  "driving",
                    "key" to BuildConfig.MAPS_API_KEY
                )
                val response = mapAPIService.getDirections(
                    output = "json",
                    parameters = parameters,
                )
                logDebug("getDirections ${response.body().toJson()}")
                _directions.postValue(JSONObject(response.body().toJson()))
            } catch (ex: Exception) {
                logDebug(ex.message)
            }
        }
    }
}