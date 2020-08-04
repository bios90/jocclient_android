package com.justordercompany.client.logic.utils

import android.util.Log
import com.justordercompany.client.extensions.addItem
import com.justordercompany.client.extensions.removeItem
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelCafe
import io.reactivex.subjects.BehaviorSubject

object BasketManager
{
    var bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    var bs_items: BehaviorSubject<ArrayList<ModelBasketItem>> = BehaviorSubject.createDefault(arrayListOf())

    fun addItem(item: ModelBasketItem)
    {
        Log.e("BasketManager", "addItem: Added item!")
        bs_items.addItem(item)
    }

    fun removeItem(item: ModelBasketItem): Boolean
    {
        return bs_items.removeItem(item)
    }

    fun updateItem(item: ModelBasketItem)
    {
        val items = bs_items.value
        if (items == null || items.size == 0)
        {
            addItem(item)
            return
        }

        val index = items.indexOfFirst { it.id_str.equals(item.id_str) }
        if (index >= 0)
        {
            items.removeAt(index)
            items.add(index, item)
            bs_items.onNext(items)
        }
    }

    fun getSum():Double
    {
        var sum = 0.0
        bs_items.value?.forEach(
            {
                sum += it.getSum()
            })

        return sum
    }

    fun getSumText():String
    {
        return "${getSum().formatAsMoney()} р."
    }
}