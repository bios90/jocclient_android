package com.justordercompany.client.logic.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

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
        var menu: ArrayList<ModelMenuCategory>? = null
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
}