package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ModelReview(

        override var id: Int? = null,
        @SerializedName("client")
        var user: ModelUser? = null,
        var rating: Int? = null,
        var date: Date? = null,
        var text: String? = null
) : Serializable, ObjectWithId