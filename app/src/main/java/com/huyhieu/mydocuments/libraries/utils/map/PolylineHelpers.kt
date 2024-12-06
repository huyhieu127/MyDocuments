package com.huyhieu.mydocuments.libraries.utils.map

import android.animation.ValueAnimator
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

object PolylineHelpers {
    private val styleGradient by lazy {
        StyleSpan(StrokeStyle.gradientBuilder(Color.RED, Color.YELLOW).build())
    }
    private val styleRed by lazy {
        StyleSpan(StrokeStyle.colorBuilder(Color.RED).build())
    }

    fun drawPolyline(
        googleMap: GoogleMap?,
        listLatLng: MutableList<LatLng>?,
        @ColorInt color: Int,
        onEachLatLng: ((LatLng) -> Unit)? = null,
        onComeToEnd: ((PolylineOptions) -> Unit)? = null
    ): Polyline? {
        googleMap ?: return null
        listLatLng ?: return null
        if (listLatLng.isEmpty()) {
            return null
        }
        val polylineOptions = PolylineOptions().width(12F)
            .color(color)
            .geodesic(true)//Smooth line, set false when don't need smooth
            .startCap(RoundCap())
            .endCap(RoundCap())
            .jointType(JointType.ROUND)
            .addSpan(styleGradient)
            .add(listLatLng[0])
//        listLatLng.forEach {
//            polylineOptions.add(it)
//            onEachLatLng?.invoke(it)
//        }

        val polyline = googleMap.addPolyline(polylineOptions)

        // Tạo ValueAnimator để vẽ polyline theo thời gian
        val animator = ValueAnimator.ofInt(1, listLatLng.size)
        animator.duration = 200 // Độ dài animation (ms)
        animator.addUpdateListener { animation ->
            val index = animation.animatedValue as Int
            if (index <= listLatLng.size) {
                val newPoints = listLatLng.subList(0, index)
                polyline.points = newPoints
                onEachLatLng?.invoke(listLatLng[index - 1])
            }
        }
        animator.start()
        onComeToEnd?.invoke(polylineOptions)
        return polyline
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