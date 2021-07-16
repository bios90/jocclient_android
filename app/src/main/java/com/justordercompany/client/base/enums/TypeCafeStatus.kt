package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getColorMy

enum class TypeCafeStatus
{
    @SerializedName("empty")
    EMPTY,
    @SerializedName("half")
    HALF,
    @SerializedName("full")
    FULL,
    @SerializedName("preorder")
    PREORDER;


    fun getColorId(): Int
    {
        return when (this)
        {
            EMPTY -> R.color.biryza
            HALF -> R.color.green
            FULL, PREORDER -> R.color.orange
        }
    }
}