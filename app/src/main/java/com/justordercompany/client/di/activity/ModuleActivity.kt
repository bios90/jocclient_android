package com.justordercompany.client.di.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.logic.utils.MessagesManager
import com.justordercompany.client.logic.view_models.MyVmFactory
import dagger.Module
import dagger.Provides

@Module
class ModuleActivity(val activity:AppCompatActivity)
{
    @Provides
    fun provideActivity():AppCompatActivity
    {
        return activity
    }

    @Provides
    fun provideLayoutInflater(): LayoutInflater
    {
        return LayoutInflater.from(activity)
    }

    @Provides
    fun provideMyVmFactory():MyVmFactory
    {
        return MyVmFactory(activity)
    }

    @Provides
    fun provideMessagesManager():MessagesManager
    {
        return MessagesManager(activity)
    }
}