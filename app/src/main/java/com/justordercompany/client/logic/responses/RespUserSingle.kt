package com.justordercompany.client.logic.responses

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelUser


class RespUserSingle
    (
        @SerializedName("data") var user: ModelUser? = null
) : BaseResponse()