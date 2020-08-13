package com.justordercompany.client.logic.utils

import android.util.Log
import com.justordercompany.client.extensions.addItem
import com.justordercompany.client.extensions.addItems
import com.justordercompany.client.extensions.clear
import com.justordercompany.client.extensions.removeItem
import com.justordercompany.client.logic.models.*
import io.reactivex.subjects.BehaviorSubject
import java.lang.RuntimeException

object BasketManager
{
    var bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    var bs_items: BehaviorSubject<ArrayList<ModelBasketItem>> = BehaviorSubject.createDefault(arrayListOf())

    fun addItem(item: ModelBasketItem)
    {
        this.bs_items.addItem(item)
    }

    fun addItems(items: List<ModelBasketItem>)
    {
        this.bs_items.addItems(items)
    }

    fun removeItem(item: ModelBasketItem): Boolean
    {
        return this.bs_items.removeItem(item)
    }

    fun clearBasket()
    {
        this.bs_items.clear()
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

    fun getSum(): Double
    {
        var sum = 0.0
        bs_items.value?.forEach(
            {
                sum += it.getSum()
            })

        return sum
    }

    fun getSumText(): String
    {
        return "${getSum().formatAsMoney()} р."
    }

    fun setOrder(order: ModelOrder)
    {
        val items = order.items
        val cafe_menu = bs_cafe.value?.menu

        if (cafe_menu == null)
        {
            throw RuntimeException("**** Error cafe not setted ****")
        }

        if (items.isNullOrEmpty())
        {
            return
        }

        val items_to_set_in_basket: ArrayList<ModelBasketItem> = arrayListOf()
        val all_items_in_cafe = cafe_menu.getAllItems()

        items.forEach(
            { item_basket ->

                val product_id = item_basket.product?.id ?: return@forEach
                val product_in_menu: ModelProduct = all_items_in_cafe.findById(product_id) ?: return@forEach

                val new_item = ModelBasketItem()
                new_item.product = product_in_menu
                new_item.sugar = item_basket.sugar

                applyWeightsFromOrder(new_item, item_basket, product_in_menu)
                applyMilksFromOrder(new_item, item_basket, product_in_menu)
                applyAddablesFromOrder(new_item, item_basket, product_in_menu)

                items_to_set_in_basket.add(new_item)
            })

        bs_items.addItems(items_to_set_in_basket)
    }

    private fun applyWeightsFromOrder(new_item: ModelBasketItem, old_item: ModelBasketItem, product_in_menu: ModelProduct)
    {
        val selected_weight_id = old_item.weight?.id
        if (selected_weight_id != null)
        {
            val weight: ModelAddableValue? = product_in_menu.weights?.findById(selected_weight_id)
            if (weight != null)
            {
                new_item.weight = weight
            }
            else
            {
                new_item.weight = product_in_menu.weights?.getOrNull(0)
            }
        }
    }

    private fun applyMilksFromOrder(new_item: ModelBasketItem, old_item: ModelBasketItem, product_in_menu: ModelProduct)
    {
        val selected_milk_id = old_item.milk?.id
        if (selected_milk_id != null)
        {
            val milk: ModelAddableValue? = product_in_menu.milks?.findById(selected_milk_id)
            if (milk != null)
            {
                new_item.milk = milk
            }
            else
            {
                new_item.milk = product_in_menu.milks?.getOrNull(0)
            }
        }
    }

    private fun applyAddablesFromOrder(new_item: ModelBasketItem, old_item: ModelBasketItem, product_in_menu: ModelProduct)
    {
        old_item.addables?.forEach(
            {
                val selected_addable_id = it.id ?: return@forEach
                val addable_in_product: ModelAddableValue = product_in_menu.addables?.findById(selected_addable_id) ?: return@forEach

                if (old_item.addables == null)
                {
                    old_item.addables = arrayListOf()
                }

                old_item.addables!!.add(addable_in_product)
            })
    }
}