package com.justordercompany.client.base.diff

import androidx.recyclerview.widget.DiffUtil
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelProduct

class DiffBasketItem(private val items_old: List<ModelBasketItem>, private val items_new: List<ModelBasketItem>)
    : DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        return item_new.id_str.equals(item_old.id_str)
    }

    override fun getOldListSize(): Int
    {
        return items_old.size
    }

    override fun getNewListSize(): Int
    {
        return items_new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if (item_new.product != item_old.product)
        {
            return false
        }

        if (item_new.getSum() != item_old.getSum())
        {
            return false
        }

        if (item_new.weight != item_old.weight)
        {
            return false
        }

        if (item_new.sugar != item_old.sugar)
        {
            return false
        }

        if (item_new.milk != item_old.milk)
        {
            return false
        }

        return true
    }
}