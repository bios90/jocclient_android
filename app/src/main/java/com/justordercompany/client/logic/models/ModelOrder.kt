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
        var type: TypeProduct? = null,
        override var created: Date?,
        override var updated: Date?,
        override var deleted: Date?,
        var status: TypeOrderStatus? = null,
        var paid: Boolean? = null,
        var date: Date? = null,
//        var paid_at: String? = null,
        var comment: String? = null,
        var cafe_id: Int? = null,
        var products: ArrayList<ModelProduct>? = null
) : Serializable, ObjectWithDates, ObjectWithId
{
    fun getProductNamesList():String?
    {
        if(products == null || products!!.size == 0)
        {
            return null
        }

        val names = products!!.map({ it.name }).filterNotNull()
        return StringManager.listOfStringToSingle(names,", ")
    }
}