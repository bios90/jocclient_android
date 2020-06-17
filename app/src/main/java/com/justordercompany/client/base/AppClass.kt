package com.justordercompany.client.base

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.justordercompany.client.R
import com.justordercompany.client.di.application.ComponentApplication
import com.justordercompany.client.di.application.DaggerComponentApplication
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy

class AppClass : Application()
{
    companion object
    {
        lateinit var app: AppClass
        lateinit var app_component: ComponentApplication
        lateinit var gson: Gson
    }

    override fun onCreate()
    {
        super.onCreate()
        app = this

        app_component = DaggerComponentApplication.builder()
                .build()

        gson = GsonBuilder()
                .setDateFormat(getStringMy(R.string.format_for_server))
                .create()
        test()
    }

    fun test()
    {
        val trans = getColorMy(R.color.transparent)
        val trans2 = getColorMy(R.color.transparent)

        Log.e("AppClass", "test: trans 1 is $trans, trans2 is $trans2")
    }
}