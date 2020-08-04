package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy

enum class TypeProduct
{
    @SerializedName("hot")
    HOT,
    @SerializedName("cold")
    COLD,
    @SerializedName("dessert")
    SNACK;

    fun getNameForHeader():String
    {
        when(this)
        {
            HOT->return getStringMy(R.string.hot_drinks)
            COLD->return getStringMy(R.string.cold_drinks)
            SNACK->return getStringMy(R.string.snacks)
        }
    }
}