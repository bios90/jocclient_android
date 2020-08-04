package com.justordercompany.client.logic.utils

import android.text.format.DateFormat
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.lang.RuntimeException
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
        val FORMAT_FOR_SERVER = getStringMy(R.string.format_for_server)
        val FORMAT_FOR_SERVER_WITHOUT_SECONDS = getStringMy(R.string.format_for_server_without_seconds)
    }
}

//Date extensions
fun Date.formatToString(format: String): String
{
    return SimpleDateFormat(format).format(this).toString()
}

fun Date.formatToString(): String
{
    return this.formatToString(DateManager.FORMAT_YYMMDD_FOR_DISPLAY_SHORT)
}

fun Date.getCurrentTime(): String
{
    return this.formatToString(DateManager.FORMAT_TIME)
}

fun Date.getHour(): Int
{
    val date_joda = DateTime(this)
    return date_joda.hourOfDay
}

fun Date.getMinute(): Int
{
    val date_joda = DateTime(this)
    return date_joda.minuteOfHour
}

fun Date.addMinutes(minutes: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusMinutes(minutes).toDate()
}

fun Date.minusMinutes(minutes: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusMinutes(minutes).toDate()
}

fun Date.addHours(hours: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusHours(hours).toDate()
}

fun Date.minusHours(hours: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusHours(hours).toDate()
}

fun Date.addDays(days: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusDays(days).toDate()
}

fun Date.minusDays(days: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusDays(days).toDate()
}

fun Date.addMonths(months: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusMonths(months).toDate()
}

fun Date.minusMonths(months: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusMonths(months).toDate()
}

fun Date.isToday(): Boolean
{
    val date_joda = DateTime(this)
    return LocalDate.now().compareTo(date_joda.toLocalDate()) == 0
}

fun Date.isYesterday(): Boolean
{
    val date_joda = DateTime(this)
    return LocalDate.now().minusDays(1).compareTo(date_joda.toLocalDate()) == 0
}

fun Date.setMinutesMy(minutes: Int): Date
{
    if (minutes in (0 until 60) == false)
    {
        throw RuntimeException("***** Error wrong minutes to set *****")
    }

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MINUTE, minutes)
    return calendar.time
}

fun Date.setHoursMy(hours: Int): Date
{
    if (hours in (0 until 24) == false)
    {
        throw RuntimeException("***** Error wrong hours to set *****")
    }

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    return calendar.time
}

fun areAtSameDay(date_1: Date, date_2: Date): Boolean
{
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = date_1
    cal2.time = date_2
    val same_day = (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
            && (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
    return same_day
}
