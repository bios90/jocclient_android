package com.justordercompany.client.logic.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.justordercompany.client.base.enums.TypeProduct
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
        var menu: ModelMenu? = null,
        var distance: Int? = null,
        @SerializedName("working_hours")
        var working_hours_str: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("photo")
        var images: ArrayList<BaseImage>? = null
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

fun ModelCafe.hasHotDrinks(): Boolean
{
    return this.menu?.hot_drinks != null && this.menu!!.hot_drinks!!.size > 0
}

fun ModelCafe.hasColdDrinks(): Boolean
{
    return this.menu?.cold_drinks != null && this.menu!!.cold_drinks!!.size > 0
}

fun ModelCafe.hasSnacks(): Boolean
{
    return this.menu?.snacks != null && this.menu!!.snacks!!.size > 0
}

fun ModelCafe.getProductsOfType(type: TypeProduct): ArrayList<ModelProduct>?
{
    when (type)
    {
        TypeProduct.HOT -> return menu?.hot_drinks
        TypeProduct.COLD -> return menu?.cold_drinks
        TypeProduct.SNACK -> return menu?.snacks
    }
}

fun ModelCafe.hasThreeMenuCategs(): Boolean
{
    return this.hasHotDrinks() && this.hasColdDrinks() && this.hasSnacks()
}

fun ModelCafe.hasTwoMenuCategs(): Boolean
{
    if (this.hasThreeMenuCategs())
    {
        return false
    }

    val have_two = (hasHotDrinks() && hasColdDrinks())
            || (hasHotDrinks() && hasSnacks())
            || (hasColdDrinks() && hasSnacks())

    return have_two
}

fun ModelCafe.hasOneMenuCategs(): Boolean
{
    if (this.hasThreeMenuCategs() || this.hasTwoMenuCategs())
    {
        return false
    }

    val have_one = this.hasHotDrinks() || this.hasColdDrinks() || this.hasSnacks()

    return have_one
}