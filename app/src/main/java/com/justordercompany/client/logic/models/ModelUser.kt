package com.justordercompany.client.logic.models

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.justordercompany.client.extensions.toJsonMy
import java.util.*

class ModelUser : ObjectWithId, ObjectWithDates
{
    override var id: Int? = null
    override var created: Date? = null
    override var updated: Date? = null
    override var deleted: Date? = null

    var name: String? = null
    var email: String? = null
    var api_token: String? = null
    var phone: String? = null
    var image: BaseImage? = null
    @SerializedName("cups_count")
    var count_cups: String? = null
    @SerializedName("orders_count")
    var count_orders: String? = null
    @SerializedName("reviews_count")
    var count_reviews: String? = null

    fun isEmpty(): Boolean
    {
        return name.isNullOrEmpty() && email.isNullOrEmpty() && image == null
    }
}
