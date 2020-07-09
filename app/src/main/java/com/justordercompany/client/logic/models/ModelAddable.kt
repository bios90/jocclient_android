package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName

class ModelAddable
    (
        override var id: Int? = null,
        @SerializedName("isMainPrice")
        var is_main_price: Boolean? = null,
        var required: Boolean? = null,
        var unit: String? = null,
        var name: String? = null,
        @SerializedName("items")
        var values: ArrayList<ModelAddableValue>? = null
) : ObjectWithId