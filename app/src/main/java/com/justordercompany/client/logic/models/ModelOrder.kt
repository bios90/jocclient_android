package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.base.enums.TypeOrderStatus
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.logic.utils.strings.StringManager
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ModelOrder
    (
        override var id: Int? = null,
        override var created: Date?,
        override var updated: Date?,
        override var deleted: Date?,
        var status: TypeOrderStatus? = null,
        var paid: Boolean? = null,
        var date: Date? = null,
//        var paid_at: String? = null,
        var comment: String? = null,
        var cafe: ModelCafe? = null,
        var sum: Double? = null,
        @SerializedName("products")
        var items: ArrayList<ModelBasketItem>? = null,
        var user: ModelUser? = null
        ) : Serializable, ObjectWithDates, ObjectWithId
{
    fun getProductNamesList(): String?
    {
        if (items == null || items!!.size == 0)
        {
            return null
        }

        val names = items!!.map({ it.product?.name }).filterNotNull()
        return StringManager.listOfStringToSingle(names, ", ")
    }

    fun canBeCancelled(): Boolean
    {
        return this.status == TypeOrderStatus.NEW || this.status == TypeOrderStatus.PAID || this.status == TypeOrderStatus.PROCESS
    }
}