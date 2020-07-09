package com.justordercompany.client.logic.models

class ModelAddableValue
    (
        override var id: Int? = null,
        var value: String? = null,
        var price: Double? = null
) : ObjectWithId