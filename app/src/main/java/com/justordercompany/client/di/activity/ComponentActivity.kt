package com.justordercompany.client.di.activity

import com.justordercompany.client.ui.screens.act_auth.ActAuth
import com.justordercompany.client.ui.screens.act_main.ActMain
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act_main:ActMain)
    fun inject(act_main:ActAuth)
}