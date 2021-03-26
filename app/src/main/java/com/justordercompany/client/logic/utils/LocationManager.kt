package com.justordercompany.client.logic.utils

import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BusMainEvents
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getStringMy
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class LocationManager
{
    companion object
    {
        fun getDistanceInMeters(l1: LatLng, l2: LatLng): Int
        {
            val startPoint = Location("locationA")
            startPoint.latitude = l1.latitude
            startPoint.longitude = l1.longitude

            val endPoint = Location("locationA")
            endPoint.latitude = l2.latitude
            endPoint.longitude = l2.longitude

            val distance = startPoint.distanceTo(endPoint)

            return distance.toInt()
        }

        fun getDistanceText(l1: LatLng, l2: LatLng): String
        {
            val distance = getDistanceInMeters(l1, l2)

            return getDistanceText(distance)
        }

        fun getDistanceText(distance: Int): String
        {
            if (distance < 1000)
            {
                return "$distance ${getStringMy(R.string.m)}"
            }

            val text = String.format("%.1f", distance.toFloat() / 1000)
            return "$text ${getStringMy(R.string.km)}"
        }
    }

    val bs_location: BehaviorSubject<Location> = BehaviorSubject.create()
    private val composite_disposable_location = CompositeDisposable()

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
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(30000)
                .setSmallestDisplacement(50f)

        return rx_location.location().updates(locationRequest)
    }


    fun startGeoTracker()
    {
        getLocationUpdates()
                .subscribe(
                    {
                        bs_location.onNext(it)
                    })
                .disposeBy(composite_disposable_location)

    }

    fun stopGeoTracker()
    {
        composite_disposable_location.clear()
    }
}

fun Location.toLatLng(): LatLng
{
    return LatLng(this.latitude, this.longitude)
}