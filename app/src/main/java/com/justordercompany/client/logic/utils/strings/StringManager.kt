package com.justordercompany.client.logic.utils.strings

import android.text.SpannedString
import android.text.TextUtils
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.logic.utils.DateManager
import com.justordercompany.client.logic.utils.formatToString
import java.lang.StringBuilder
import java.util.*

class StringManager
{
    companion object
    {
        fun listOfStringToSingle(strings: List<String>): String
        {
            return listOfStringToSingle(strings, "\n")
        }

        fun listOfStringToSingle(strings: List<String>, separator: String): String
        {
            val sb = StringBuilder()
            for (element in strings)
            {
                sb.append(element)
                if (strings.indexOf(element) != strings.size - 1)
                {
                    sb.append(separator)
                }
            }

            return sb.toString()
        }

        fun getNameForNewFile(extension: String): String
        {
            var name = Date().formatToString(DateManager.FORMAT_FILE_NAME)
            name += ".$extension"
            return name
        }

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

fun getOffertText(): SpannedString
{
    val span_1 = MySpan()
            .setColor(getColorMy(R.color.gray6))
            .setFontRes(R.font.exo_reg)
            .setText("Я согласен с условиями ")

    val span_2 = MySpan()
            .setColor(getColorMy(R.color.gray8))
            .setFontRes(R.font.exo_bold)
            .setText("офферты")

    return TextUtils.concat(span_1.build(), span_2.build()) as SpannedString
}
