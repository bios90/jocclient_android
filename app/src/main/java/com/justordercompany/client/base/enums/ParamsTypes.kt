package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName

enum class PmSortDirection
{
    ASC,
    DESC
}

enum class PmCafeSort
{
    @SerializedName("rating")
    RATING,
    @SerializedName("distance")
    DISTANCE
}