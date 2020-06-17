package com.justordercompany.client.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.logic.models.ModelMapPos

fun GoogleMap.moveCameraToPos(lat_lng: LatLng, zoom: Float = 10f)
{
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, zoom), 300, null)
}

fun GoogleMap.moveCameraToPos(map_pos: ModelMapPos)
{
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(map_pos.latLng, map_pos.zoom), 300, null)
}