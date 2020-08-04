package com.justordercompany.client.logic.models

import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy
import java.io.Serializable

class ModelAddableValue
    (
        override var id: Int? = null,
        var value: String? = null,
        var price: Double? = null
) : Serializable, ObjectWithId
{
    companion object
    {
        fun getAddableSugar(): ArrayList<ModelAddableValue>
        {
            val sugar_0 = ModelAddableValue(null, getStringMy(R.string.sugar_free), 0.0)
            val sugar_1 = ModelAddableValue(null, "1", 0.0)
            val sugar_2 = ModelAddableValue(null, "2", 0.0)
            val sugar_3 = ModelAddableValue(null, "3", 0.0)

            return arrayListOf(sugar_0, sugar_1, sugar_2, sugar_3)
        }
    }
}

fun List<ModelAddableValue>.getValuesByPoses(poses: ArrayList<Int>): ArrayList<ModelAddableValue>
{
    val selected: ArrayList<ModelAddableValue> = arrayListOf()
    this.forEachIndexed(
        { index, addable ->

            if (poses.contains(index))
            {
                selected.add(addable)
            }
        })

    return selected
}

fun List<ModelAddableValue>.getPriceSum(): Double
{
    var price: Double = 0.0
    this.forEach(
        {
            it.price?.let(
                {
                    price += it
                })
        })
    return price
}