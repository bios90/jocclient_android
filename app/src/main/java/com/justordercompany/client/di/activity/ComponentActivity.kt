package com.justordercompany.client.di.activity

import com.justordercompany.client.ui.act_main.ActMain
import dagger.Component
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act_main:ActMain)
}