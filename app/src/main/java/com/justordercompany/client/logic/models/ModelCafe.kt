package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName

class ModelCafe(
        override var id: Int? = null,
        @SerializedName("canOrder")
        var can_order: Boolean? = null,
        var name: String? = null,
        var lat: Double? = null,
        @SerializedName("lng")
        var lon: Double? = null,
        var logo: BaseImage
) : ObjectWithId