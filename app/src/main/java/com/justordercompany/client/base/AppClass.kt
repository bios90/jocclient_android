package com.justordercompany.client.base

import android.app.Application
import com.justordercompany.client.di.application.ComponentApplication
import com.justordercompany.client.di.application.DaggerComponentApplication

class AppClass : Application()
{
    companion object
    {
        lateinit var app: AppClass
        lateinit var app_component: ComponentApplication
    }

    override fun onCreate()
    {
        super.onCreate()
        app = this
        app_component = DaggerComponentApplication.builder()
                .build()
    }
}