package com.justordercompany.client.logic.models

import com.google.gson.annotations.SerializedName
import com.justordercompany.client.base.enums.TypeProduct

class ModelMenu
    (
        @SerializedName("hot")
        var hot_drinks:ArrayList<ModelProduct>? = ArrayList(),
        @SerializedName("cold")
        var cold_drinks:ArrayList<ModelProduct>? = ArrayList(),
        @SerializedName("dessert")
        var snacks:ArrayList<ModelProduct>? = ArrayList()
)
{
//    init
//    {
//        //Todo manuall setting types here, remove later if gets from server
//        hot_drinks?.forEach(
//            {
//                it.type = TypeProduct.HOT
//            })
//
//        cold_drinks?.forEach(
//            {
//                it.type = TypeProduct.COLD
//            })
//
//        snacks?.forEach(
//            {
//                it.type = TypeProduct.SNACK
//            })
//    }

    fun getAllItems():ArrayList<ModelProduct>
    {
        val all_items:ArrayList<ModelProduct> = arrayListOf()

        if(!hot_drinks.isNullOrEmpty())
        {
            all_items.addAll(hot_drinks!!)
        }

        if(!cold_drinks.isNullOrEmpty())
        {
            all_items.addAll(cold_drinks!!)
        }

        if(!snacks.isNullOrEmpty())
        {
            all_items.addAll(snacks!!)
        }

        return all_items
    }
}