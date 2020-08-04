package com.justordercompany.client.logic.responses

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelOrder

class RespOrderSingle(
        @SerializedName("data") var order: ModelOrder? = null
) : BaseResponse()