package com.justordercompany.client.base.enums

enum class TypeTab
{
    PROFILE,
    MAP,
    LIST;

    fun getPos(): Int
    {
        when (this)
        {
            PROFILE -> return 0
            MAP -> return 1
            LIST -> return 2
        }
    }

}