package com.justordercompany.client.base.diff

import androidx.recyclerview.widget.DiffUtil
import com.justordercompany.client.logic.models.ModelOrder

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
        if (order_new.date?.compareTo(order_old.date) != 0)
        {
            return false
        }

        if (order_new.created?.compareTo(order_old.created) != 0)
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