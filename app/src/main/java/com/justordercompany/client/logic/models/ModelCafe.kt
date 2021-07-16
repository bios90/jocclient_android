package com.justordercompany.client.logic.models

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.justordercompany.client.R
import com.justordercompany.client.base.enums.TypeCafeStatus
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.base.enums.TypeSocialIcon
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.utils.LocationManager

class ModelCafe(
        override var id: Int? = null,
        @SerializedName("canOrder", alternate = ["can_order"])
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
        var images: ArrayList<BaseImage>? = null,
        var reviews: ArrayList<ModelReview>? = null,
        var is_favorite: String? = null,
        var status:TypeCafeStatus? = null,

        var social_instagram: String? = null,
        var social_vk: String? = null,
        var social_facebook: String? = null,
        var social_twitter: String? = null,
        var social_whatsapp: String? = null

) : ObjectWithId
{
    fun isFavorite(): Boolean
    {
        return is_favorite.equals("true")
    }

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

    fun getAllImagesWithLogo(): ArrayList<BaseImage>
    {
        val all_images: ArrayList<BaseImage> = arrayListOf()

        this.logo?.let(
            {
                all_images.add(it)
            })

        if (!this.images.isNullOrEmpty())
        {
            all_images.addAll(this.images!!)
        }

        return all_images
    }

    fun getSocials(): Map<TypeSocialIcon, String?>
    {
        val url_whatwapp: String? = run(
            {
                if (social_whatsapp.isNullOrEmpty())
                {
                    return@run null
                }
                else if (social_whatsapp?.startsWith("https://", true) == true)
                {
                    return@run social_whatsapp
                }
                else
                {
                    return@run "https://wa.me/" + social_whatsapp!!.filter({ it.isDigit() })
                }
            })

        return mapOf(
            Pair(TypeSocialIcon.INSTAGRAM, social_instagram),
            Pair(TypeSocialIcon.VK, social_vk),
            Pair(TypeSocialIcon.FACEBOOK, social_facebook),
            Pair(TypeSocialIcon.WHATSAPP, url_whatwapp),
            Pair(TypeSocialIcon.TWITTER, social_twitter)
        )
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