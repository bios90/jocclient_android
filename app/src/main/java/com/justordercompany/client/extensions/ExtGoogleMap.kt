package com.justordercompany.client.extensions

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.logic.models.ModelMapPos
import android.location.Location.distanceBetween
import com.google.android.gms.maps.model.VisibleRegion



fun GoogleMap.moveCameraToPos(lat_lng: LatLng, zoom: Float = 10f)
{
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, zoom), 300, null)
}

fun GoogleMap.moveCameraToPos(map_pos: ModelMapPos)
{
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(map_pos.latLng, map_pos.zoom), 300, null)
}

fun GoogleMap.getVisibleDistance():Float
{
    val visibleRegion = this.getProjection().getVisibleRegion()

    val farRight = visibleRegion.farRight
    val farLeft = visibleRegion.farLeft
    val nearRight = visibleRegion.nearRight
    val nearLeft = visibleRegion.nearLeft

    val distanceWidth = FloatArray(2)
    Location.distanceBetween(
        (farRight.latitude + nearRight.latitude) / 2,
        (farRight.longitude + nearRight.longitude) / 2,
        (farLeft.latitude + nearLeft.latitude) / 2,
        (farLeft.longitude + nearLeft.longitude) / 2,
        distanceWidth
    )


    val distanceHeight = FloatArray(2)
    Location.distanceBetween(
        (farRight.latitude + nearRight.latitude) / 2,
        (farRight.longitude + nearRight.longitude) / 2,
        (farLeft.latitude + nearLeft.latitude) / 2,
        (farLeft.longitude + nearLeft.longitude) / 2,
        distanceHeight
    )

    val distance: Float

    if (distanceWidth[0] > distanceHeight[0])
    {
        distance = distanceWidth[0]
    }
    else
    {
        distance = distanceHeight[0]
    }

    return distance
}