package com.justordercompany.client.base.enums

import com.google.gson.annotations.SerializedName
import java.lang.RuntimeException

enum class TypeCafe
{
    ALL,
    @SerializedName("cafe")
    CAFE,
    @SerializedName("coffee_spot")
    COFFEE_SPOT;

    fun getRadioPos(): Int
    {
        when (this)
        {
            ALL -> return 0
            CAFE -> return 1
            COFFEE_SPOT -> return 2
        }
    }

    companion object
    {
        fun initFromRadioPos(pos: Int): TypeCafe
        {
            when (pos)
            {
                0 -> return ALL
                1 -> return CAFE
                2 -> return COFFEE_SPOT
                else ->
                {
                    throw RuntimeException("**** Errr wrong position ****")
                }
            }
        }
    }
}