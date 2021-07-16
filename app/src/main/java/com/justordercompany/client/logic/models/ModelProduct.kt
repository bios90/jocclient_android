package com.justordercompany.client.logic.models

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.justordercompany.client.R
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.utils.formatAsMoney
import java.io.Serializable

class ModelProduct
    (
        override var id: Int? = null,
        var description: String? = null,
        var name: String? = null,
        var image: BaseImage? = null,
        var price: Double? = null,
        @SerializedName("milk")
        var milks: ArrayList<ModelAddableValue>? = null,
        @SerializedName("weight")
        var weights: ArrayList<ModelAddableValue>? = null,
        @SerializedName("additive")
        var addables: ArrayList<ModelAddableValue>? = null,
        @SerializedName("category")
        var type: TypeProduct? = null
) : Serializable, ObjectWithId
{
    fun hasMilks(): Boolean
    {
        return milks != null && milks!!.size > 0
    }

    fun hasWeights(): Boolean
    {
        return weights != null && weights!!.size > 0
    }

    fun hasAddables(): Boolean
    {
        return addables != null && addables!!.size > 0
    }

    fun getMinPrice(): Double?
    {
        if (this.weights != null && this.weights!!.size > 0)
        {
            return weights!!.sortedBy({ it.price }).get(0).price
        }
        else if (price != null)
        {
            return price
        }

        return null
    }

    fun getPriceText(): String?
    {
        val price = getMinPrice() ?: return null
        var text = "${price.formatAsMoney()} р."
//        if (hasWeights() || hasMilks() || hasAddables())
//        {
//            text = "${getStringMy(R.string.from)}\n$text"
//        }

        return text
    }

    fun toDefaultBasketItem(): ModelBasketItem
    {
        val item = ModelBasketItem(this)

        if (hasWeights())
        {
            item.weight = weights!!.get(0)
        }

        if (this.type == TypeProduct.SNACK)
        {
            return item
        }

        item.sugar = 0

        if (hasMilks())
        {
            item.milk = milks!!.get(0)
        }

        return item
    }

    fun getPosesOfAddables(addables: List<ModelAddableValue>): List<Int>
    {
        val poses: ArrayList<Int> = arrayListOf()
        if (!this.hasAddables())
        {
            return poses
        }

        addables.forEach(
            { added_addable ->

                this.addables!!.forEachIndexed(
                    { index, product_addable ->

                        if (added_addable.id == product_addable.id)
                        {
                            poses.add(index)
                        }
                    })
            })

        return poses
    }
}
