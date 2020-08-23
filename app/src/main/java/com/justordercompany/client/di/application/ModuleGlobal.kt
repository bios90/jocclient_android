package com.justordercompany.client.di.application

import com.justordercompany.client.base.BusMainEvents
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.NotificationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleGlobal
{
    @Singleton
    @Provides
    fun provideBusMain():BusMainEvents
    {
        return BusMainEvents()
    }

    @Singleton
    @Provides
    fun provideLocationManager():LocationManager
    {
        return LocationManager()
    }

    @Singleton
    @Provides
    fun provideNotificationManager():NotificationManager
    {
        return NotificationManager()
    }
}