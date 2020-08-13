package com.justordercompany.client.base.diff

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.utils.DateManager
import com.justordercompany.client.logic.utils.areDatesEqualForDiff
import com.justordercompany.client.logic.utils.formatToString

class DiffOrders(val orders_old: List<ModelOrder>, val orders_new: List<ModelOrder>) : DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val order_new = orders_new.get(newItemPosition)
        val order_old = orders_old.get(oldItemPosition)

        if (order_new.id == null || order_old.id == null)
        {
            return false
        }

        return order_new.id == order_old.id
    }

    override fun getOldListSize(): Int
    {
        return orders_old.size
    }

    override fun getNewListSize(): Int
    {
        return orders_new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val order_new = orders_new.get(newItemPosition)
        val order_old = orders_old.get(oldItemPosition)

        //Todo make later check for cafe name adress and logo
        if (!areDatesEqualForDiff(order_new.date,order_old.date))
        {
            return false
        }

        if (!areDatesEqualForDiff(order_new.created,order_old.created))
        {
            return false
        }

        if (!areDatesEqualForDiff(order_new.updated,order_old.updated))
        {
            return false
        }

        if (order_new.status != order_old.status)
        {
            return false
        }

        if(!order_new.getProductNamesList().equals(order_old.getProductNamesList()))
        {
            return false
        }

        return true
    }
}