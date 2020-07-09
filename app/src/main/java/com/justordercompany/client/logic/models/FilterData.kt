package com.justordercompany.client.logic.models

import com.justordercompany.client.base.enums.TypeCafe
import java.io.Serializable

data class FilterData
    (
        var price_min: Int = 0,
        var price_max: Int = 1000,
        var type_cafe: TypeCafe = TypeCafe.ALL,
        var rating: Float = 4.0f
) : Serializable