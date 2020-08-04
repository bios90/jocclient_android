package com.justordercompany.client.base.enums

import com.justordercompany.client.logic.models.*
import java.lang.RuntimeException

enum class TypeTabCafe
{
    CAFE,
    HOT,
    COLD,
    SNACKS,
    BASKET;


    companion object
    {
        fun initFromPagerPos(pos:Int,cafe: ModelCafe):TypeTabCafe
        {

            if(pos == 0)
            {
                return CAFE
            }
            else if(pos == 1)
            {
                if(cafe.hasHotDrinks())
                {
                    return HOT
                }
                else if(cafe.hasColdDrinks())
                {
                    return COLD
                }
                else if(cafe.hasSnacks())
                {
                    return SNACKS
                }
            }
            else if(pos == 2)
            {
                if(cafe.hasHotDrinks() && cafe.hasColdDrinks())
                {
                    return COLD
                }
                else if(!cafe.hasHotDrinks() && cafe.hasColdDrinks() && cafe.hasSnacks())
                {
                    return SNACKS
                }
                else
                {
                    return BASKET
                }
            }
            else if(pos == 3)
            {
                if(cafe.hasThreeMenuCategs())
                {
                    return SNACKS
                }
                else
                {
                    return BASKET
                }
            }
            else if(pos == 4)
            {
                return BASKET
            }

            throw RuntimeException("**** Error could not get yab of pos $pos ****")
        }
    }

    fun getPos(cafe: ModelCafe): Int?
    {
        when (this)
        {
            CAFE -> return 0
            HOT ->
            {
                if (cafe.hasHotDrinks())
                {
                    return 1
                }
            }

            COLD ->
            {
                if (cafe.hasColdDrinks())
                {
                    if (cafe.hasHotDrinks())
                    {
                        return 2
                    }

                    return 1
                }
            }

            SNACKS ->
            {
                if (cafe.hasSnacks())
                {
                    if (cafe.hasHotDrinks() && cafe.hasColdDrinks())
                    {
                        return 3
                    }
                    else if (cafe.hasHotDrinks() || cafe.hasColdDrinks())
                    {
                        return 2
                    }

                    return 1
                }
            }

            BASKET ->
            {
                if (cafe.hasHotDrinks() && cafe.hasColdDrinks() && cafe.hasSnacks())
                {
                    return 4
                }

                val have_two = (cafe.hasHotDrinks() && cafe.hasColdDrinks())
                        || (cafe.hasHotDrinks() && cafe.hasSnacks())
                        || (cafe.hasColdDrinks() && cafe.hasSnacks())
                if (have_two)
                {
                    return 3
                }
                else if (cafe.hasHotDrinks() || cafe.hasColdDrinks() || cafe.hasSnacks())
                {
                    return 2
                }

                return 1
            }
        }

        return null
    }

    fun getPosInBottomNav(): Int
    {
        when (this)
        {
            CAFE -> return 0
            HOT -> return 1
            COLD -> return 2
            SNACKS -> return 3
            BASKET -> return 4
        }
    }
}