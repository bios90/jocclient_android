package com.justordercompany.client.logic.utils

import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.base.AppClass
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.Single

class LocationManager
{
    fun getLocationSingle(): Single<Location>
    {
        val observable = getLocationUpdates().take(1)
        Log.e("LocationManager", "getLocationSingle: will retunt@!1")
        return Single.fromObservable(observable)
    }

    fun getLocationUpdates(): Observable<Location>
    {
        val rx_location = RxLocation(AppClass.app)
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)

        return rx_location.location().updates(locationRequest)
    }
}

fun Location.toLatLng():LatLng
{
    return LatLng(this.latitude,this.longitude)
}