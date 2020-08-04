package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName

enum class PmSortDirection
{
    ASC,
    DESC;

    fun getServerStr():String
    {
        return this.toString().toLowerCase()
    }
}

enum class PmCafeSort
{
    @SerializedName("rating")
    RATING,
    @SerializedName("distance")
    DISTANCE;

    fun getNameForRequest():String
    {
        return name.toLowerCase()
    }
}