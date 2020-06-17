package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName

enum class TypeResponseStatus
{
    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR,
    @SerializedName("failed")
    FAILED
}