package com.justordercompany.client.logic.models

import com.google.android.gms.maps.model.LatLng

data class ModelMapPos
    (
        val latLng: LatLng,
        val zoom:Float = 10f)