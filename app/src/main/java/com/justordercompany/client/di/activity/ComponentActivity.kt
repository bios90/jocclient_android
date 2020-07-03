package com.justordercompany.client.di.activity

import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_profile_edit.ActProfileEdit
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act_main:ActMain)
    fun inject(act_main:ActProfileEdit)
}