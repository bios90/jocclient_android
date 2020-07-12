package com.justordercompany.client.logic.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.justordercompany.client.logic.utils.LocationManager

class ModelCafe(
        override var id: Int? = null,
        @SerializedName("canOrder")
        var can_order: Boolean? = null,
        var name: String? = null,
        var lat: Double? = null,
        @SerializedName("lng")
        var lon: Double? = null,
        var logo: BaseImage? = null,
        var rating: Float? = null,
        var address: String? = null,
        var menu: ArrayList<ModelMenuCategory>? = null,
        var distance: Int? = null
) : ObjectWithId
{
    fun getLatLng(): LatLng?
    {
        if (lat == null || lon == null)
        {
            return null
        }

        return LatLng(lat!!, lon!!)
    }

    fun countDistanceFrom(lat_lng: LatLng)
    {
        if (lat == null || lon == null)
        {
            return
        }

        val cafe_lat_lng = LatLng(lat!!, lon!!)
        distance = LocationManager.getDistanceInMeters(lat_lng, cafe_lat_lng)
    }
}

fun List<ModelCafe>.countDistanceFrom(lat_lng: LatLng)
{
    this.forEach(
        {
            it.countDistanceFrom(lat_lng)
        })
}