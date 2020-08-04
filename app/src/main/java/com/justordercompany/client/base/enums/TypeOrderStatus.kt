package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName

enum class TypeOrderStatus
{
    @SerializedName("new")
    NEW,
    @SerializedName("wait")
    WAIT,
    @SerializedName("paid")
    PAID,
    @SerializedName("process")
    PROCESS,
    @SerializedName("ready")
    READY,
    @SerializedName("done")
    DONE,
    @SerializedName("canceled")
    CANCELED,
}