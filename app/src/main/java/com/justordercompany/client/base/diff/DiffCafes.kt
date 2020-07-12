package com.justordercompany.client.base.diff

import androidx.recyclerview.widget.DiffUtil
import com.justordercompany.client.logic.models.ModelCafe

class DiffCafes(private val cafes_old: List<ModelCafe>, private val cafes_new: List<ModelCafe>)
    : DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val cafe_old = cafes_old.get(oldItemPosition)
        val cafe_new = cafes_new.get(newItemPosition)

        if (cafe_old.id == null || cafe_new.id == null)
        {
            return false
        }

        return cafe_old.id == cafe_new.id
    }

    override fun getOldListSize(): Int
    {
        return cafes_old.size
    }

    override fun getNewListSize(): Int
    {
        return cafes_new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val cafe_old = cafes_old.get(oldItemPosition)
        val cafe_new = cafes_new.get(newItemPosition)

        if (cafe_new.logo?.url_l?.equals(cafe_old.logo?.url_l) == false)
        {
            return false
        }

        if (cafe_new.name?.equals(cafe_old.name) == false)
        {
            return false
        }

        if (cafe_new.address?.equals(cafe_old.address) == false)
        {
            return false
        }

        if (cafe_new.rating != cafe_old.rating)
        {
            return false
        }

        return true
    }
}