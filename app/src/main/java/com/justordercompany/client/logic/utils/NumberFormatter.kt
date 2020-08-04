package com.justordercompany.client.logic.utils

import java.text.DecimalFormat

class NumberFormatter
{
    companion object
    {
        val FORMAT_MONEY = DecimalFormat("#,###,###.##")
    }
}

fun Number.formatAsMoney(): String
{
    return this.formatWith(NumberFormatter.FORMAT_MONEY)
}

fun Number.formatWith(format: DecimalFormat): String
{
    return format.format(this)
}