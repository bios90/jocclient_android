package com.justordercompany.client.logic.responses

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.extensions.toObjOrNullGson
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelUser

class RespCafeSingle
    (
        @SerializedName("data") var cafe: ModelCafe? = null
) : BaseResponse()

class RespBaseWithData(
        @SerializedName("data") var data: Map<String, String?>
) : BaseResponse()
{
    fun getInt(key: String): Int?
    {
        val str = data.get(key) ?: return null
        return str.toIntOrNull()
    }

    fun getString(key: String): String?
    {
        val str = data.get(key) ?: return null
        return str
    }

    fun <T> getObjFromData(key: String, obj_class: Class<T>): T?
    {
        val str = data.get(key) ?: return null
        return str.toObjOrNullGson(obj_class)
    }
}