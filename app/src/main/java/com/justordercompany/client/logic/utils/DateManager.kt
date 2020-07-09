package com.justordercompany.client.logic.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateManager
{
    companion object
    {
        val FORMAT_YYMMDD_FOR_DISPLAY = "dd MMMM yyyy"
        val FORMAT_YYMMDD_FOR_DISPLAY_SHORT = "dd MMM yyyy"
        val FORMAT_TIME = "HH:mm"
        val FORMAT_FILE_NAME = "dd-MM-yyyy_HH-mm-ss_SSS"
        val FORMAT_EXIF = "yyyy:MM:dd HH:mm:ss"
    }
}

//Date extensions
public fun Date.formatToString(format: String): String
{
    return SimpleDateFormat(format).format(this).toString()
}

public fun Date.formatToString(): String
{
    return this.formatToString(DateManager.FORMAT_YYMMDD_FOR_DISPLAY_SHORT)
}

public fun Date.getCurrentTime(): String
{
    return this.formatToString(DateManager.FORMAT_TIME)
}
