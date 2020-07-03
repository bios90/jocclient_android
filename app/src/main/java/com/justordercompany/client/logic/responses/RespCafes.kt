package com.justordercompany.client.logic.responses

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.logic.models.ModelCafe

class RespCafes
    (
        @SerializedName("data") var cafes: ArrayList<ModelCafe>? = null
) : BaseResponse()