package com.justordercompany.client.extensions

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.lang.RuntimeException

fun Intent.myPutExtra(name: String, obj: Any?)
{
    if (obj is Boolean)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Byte)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Char)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Short)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Int)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Long)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Float)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Double)
    {
        this.putExtra(name, obj)
    }
    else if (obj is String)
    {
        this.putExtra(name, obj)
    }
    else if (obj is CharSequence)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Serializable)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Parcelable)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Array<*>)
    {
        this.putExtra(name, obj)
    }
    else if (obj is BooleanArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is ByteArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is ShortArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is CharArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is IntArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is LongArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is FloatArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is DoubleArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Bundle)
    {
        this.putExtra(name, obj)
    }
    else
    {
        throw RuntimeException("**** Error unknown type to put as Extra ****")
    }
}

fun Intent.getIntExtraMy(name: String): Int?
{
    val num = this.getIntExtra(name, -999999)
    if (num == -999999)
    {
        return null
    }
    return num
}

fun Intent.getLongExtraMy(name: String): Long?
{
    val num = this.getLongExtra(name, -999999L)
    if (num == -999999L)
    {
        return null
    }
    return num
}

fun Intent.getBoolExtraMy(name: String): Boolean
{
    return this.getBooleanExtra(name, false)
}

fun Intent.checkIntentExtraBool(name: String): Boolean
{
    return this.getBooleanExtra(name, false)
}


