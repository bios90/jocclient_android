package com.justordercompany.client.networking

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.justordercompany.client.logic.utils.DateManager
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.JsonParseException
import java.text.ParseException



class GsonDateDeSerializer:JsonDeserializer<Date>
{
    val format_main = SimpleDateFormat(DateManager.FORMAT_FOR_SERVER)
    val format_sub = SimpleDateFormat(DateManager.FORMAT_FOR_SERVER_WITHOUT_SECONDS)

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date
    {
        try
        {
            val j = json?.getAsJsonPrimitive()?.asString
            return parseDate(j)!!
        }
        catch (e: ParseException)
        {
            throw JsonParseException(e.message, e)
        }
    }

    private fun parseDate(str:String?):Date?
    {
        if(str == null || str.trim().length == 0)
        {
            return null
        }

        try
        {
            return format_main.parse(str)
        }
        catch (pe: ParseException)
        {

        }

        return format_sub.parse(str)
    }
}