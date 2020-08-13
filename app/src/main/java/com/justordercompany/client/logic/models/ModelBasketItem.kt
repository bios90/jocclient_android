package com.justordercompany.client.logic.models

import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.strings.StringManager
import com.justordercompany.client.logic.utils.strings.getRandomString
import java.io.Serializable
import java.lang.RuntimeException

class ModelBasketItem
    (
        var product: ModelProduct? = null,
        var sugar: Int? = null,
        var weight: ModelAddableValue? = null,
        var milk: ModelAddableValue? = null,
        var addables: ArrayList<ModelAddableValue>? = null,
        var id_str: String = String.getRandomString()
) : Serializable
{
    fun getSum(): Double
    {
        if (product?.type == TypeProduct.SNACK)
        {
            return product?.price!!
        }

        var price = 0.0
        if (weight?.price == null)
        {
            throw RuntimeException("**** Error no selected weight ****")
        }

        price += weight!!.price!!

        milk?.price?.let(
            {
                price += it
            })

        addables?.forEach(
            {
                if (it.price != null)
                {
                    price += it.price!!
                }
            })

        return price
    }

    fun toModelBasketValue(): ArrayList<ModelServerBasketValue>
    {
        val items: ArrayList<ModelServerBasketValue> = arrayListOf()

        weight?.id?.let(
            {
                items.add(ModelServerBasketValue(it, sugar))
            })

        milk?.id?.let(
            {
                items.add(ModelServerBasketValue(it, sugar))
            })

        addables?.forEach(
            {
                it.id?.let(
                    {
                        items.add(ModelServerBasketValue(it, sugar))
                    })
            })

        if (items.size == 0)
        {
            throw RuntimeException("**** Error in items in product ****")
        }

        return items
    }

}

class ModelServerBasketValue
    (
        val property_id: Int,
        val sugar: Int? = null,
        val quantity: Int = 1
)


fun ArrayList<ModelBasketItem>.toServerBasketItems(): ArrayList<ArrayList<ModelServerBasketValue>>
{
    val items: ArrayList<ArrayList<ModelServerBasketValue>> = arrayListOf()
    this.forEach(
        {
            items.add(it.toModelBasketValue())
        })
    return items
}

fun ArrayList<ModelBasketItem>.getSumPrice(): Double
{
    var sum = 0.0
    BasketManager.bs_items.value?.forEach(
        {
            sum += it.getSum()
        })

    return sum
}

