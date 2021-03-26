package com.justordercompany.client.logic.responses

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.base.enums.TypeResponseStatus
import com.justordercompany.client.logic.utils.strings.StringManager
import com.justordercompany.client.networking.ServerError
import com.justordercompany.client.networking.UnknownServerError
import java.lang.RuntimeException

open class BaseResponse
    (
        val status: TypeResponseStatus? = null,
        @SerializedName("message")
        var errors: List<String>? = null
)

fun BaseResponse.getError(): RuntimeException?
{
    if (this.isSuccessful() == true)
    {
        return null
    }

    if (this.hasErrors())
    {
        val message = StringManager.listOfStringToSingle(this.errors!!)
        return ServerError(message)
    }
    else
    {
        return UnknownServerError()
    }
}

fun BaseResponse?.isSuccessful(): Boolean
{
    if (this?.status == null)
    {
        return false
    }

    return this.status == TypeResponseStatus.SUCCESS
}

fun BaseResponse?.isFailed(): Boolean
{
    if (this?.status == null)
    {
        return false
    }

    return this.status == TypeResponseStatus.FAILED
}

fun BaseResponse?.isError(): Boolean
{
    if (this?.status == null)
    {
        return false
    }

    return this.status == TypeResponseStatus.ERROR
}

fun BaseResponse?.hasErrors(): Boolean
{
    return this?.errors != null && this.errors!!.isNotEmpty()
}