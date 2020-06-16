package com.justordercompany.client.di.application

import com.justordercompany.client.base.BusMainEvents
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
}