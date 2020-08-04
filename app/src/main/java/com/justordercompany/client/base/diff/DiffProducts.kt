package com.justordercompany.client.base.diff

import androidx.recyclerview.widget.DiffUtil
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelProduct

class DiffProducts(private val products_old: List<ModelProduct>, private val products_new: List<ModelProduct>)
    : DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val product_old = products_old.get(oldItemPosition)
        val product_new = products_new.get(newItemPosition)

        if (product_old.id == null || product_new.id == null)
        {
            return false
        }

        return product_old.id == product_new.id
    }

    override fun getOldListSize(): Int
    {
        return products_old.size
    }

    override fun getNewListSize(): Int
    {
        return products_new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val product_old = products_old.get(oldItemPosition)
        val product_new = products_new.get(newItemPosition)


        if (product_new.image?.url_m?.equals(product_old.image?.url_m) == false)
        {
            return false
        }

        if (product_new.name?.equals(product_old.name) == false)
        {
            return false
        }

        if (product_new.description?.equals(product_old.description) == false)
        {
            return false
        }

        if (product_new.getMinPrice() != product_old.getMinPrice())
        {
            return false
        }

        return true
    }
}