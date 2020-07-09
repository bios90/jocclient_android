package com.justordercompany.client.logic.models

class ModelMenuCategory
    (
        override var id: Int? = null,
        var name: String? = null,
        var products: ArrayList<ModelMenuItem>? = null
) : ObjectWithId