package com.justordercompany.client.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

fun GoogleMap.moveCameraToPos(lat_lng:LatLng,zoom:Float = 10f)
{
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng,zoom),300,null)
}