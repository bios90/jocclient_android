package com.justordercompany.client.base.enums

enum class TypeTab
{
    PROFILE,
    LIST,
    MAP;


    fun getPos(): Int
    {
        when (this)
        {
            PROFILE -> return 0
            LIST -> return 1
            MAP -> return 2
        }
    }
}