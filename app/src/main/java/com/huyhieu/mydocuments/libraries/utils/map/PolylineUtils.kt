package com.huyhieu.mydocuments.libraries.utils.map

import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.PolyUtil
import org.json.JSONObject

object PolylineUtils {
    private val styleGradient by lazy {
        StyleSpan(StrokeStyle.gradientBuilder(Color.RED, Color.YELLOW).build())
    }
    private val styleRed by lazy {
        StyleSpan(StrokeStyle.colorBuilder(Color.RED).build())
    }

    fun drawPolyline(
        googleMap: GoogleMap?, listLatLng: MutableList<LatLng>?, @ColorInt color: Int
    ): Polyline? {
        googleMap ?: return null
        listLatLng ?: return null
        val options = PolylineOptions().width(12F)
            .color(color)
            .geodesic(true)
            .startCap(RoundCap())
            .endCap(RoundCap())
            .jointType(JointType.ROUND)
            .addSpan(styleGradient)
        listLatLng.forEach {
            options.add(it)
        }
        return googleMap.addPolyline(options)
    }

    fun decodePoints(json: JSONObject): MutableList<LatLng> {
        val listLatLng = mutableListOf<LatLng>()
        val jRoutes = json.getJSONArray("routes")

        /** Traversing all routes  */
        for (i in 0 until jRoutes.length()) {
            val jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
            /** Traversing all legs  */
            for (j in 0 until jLegs.length()) {
                val jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")
                /** Traversing all steps  */
                for (k in 0 until jSteps.length()) {
                    var polyline = ""
                    polyline =
                        ((jSteps.get(k) as JSONObject)["polyline"] as JSONObject)["points"] as String
                    val list = PolyUtil.decode(polyline)
                    listLatLng.addAll(list)
                }
            }
        }
        return listLatLng
    }
}