package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName

class ModelMenuItem
    (
        override var id: Int? = null,
        var description: String? = null,
        var name: String? = null,
        var image: BaseImage? = null,
        var price: Double? = null,
        @SerializedName("additionalCategories")
        var addables: ArrayList<ModelAddable>? = null
) : ObjectWithId