package com.justordercompany.client.logic.utils

import java.lang.StringBuilder
import java.util.*

class StringManager
{
    companion object
    {

    }
}

fun String.Companion.getRandomString(size: Int = 20): String
{
    val all_chars: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val sb: StringBuilder = StringBuilder(size)
    val random: Random = Random()

    for (i in 0..size)
    {
        sb.append(all_chars.get(random.nextInt(all_chars.length)))
    }

    return sb.toString()
}