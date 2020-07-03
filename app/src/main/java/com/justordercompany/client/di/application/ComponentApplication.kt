package com.justordercompany.client.di.application

import com.justordercompany.client.di.activity.ComponentActivity
import com.justordercompany.client.di.activity.ModuleActivity
import com.justordercompany.client.ui.screens.act_main.VmActMain
import com.justordercompany.client.ui.screens.act_main.tabs.map.VmTabMap
import com.justordercompany.client.ui.screens.act_main.tabs.profile.VmTabProfile
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleNetworking::class, ModuleGlobal::class])
interface ComponentApplication
{
    fun inject(vm: VmActMain)
    fun inject(vm: VmTabMap)
    fun inject(vm: VmTabProfile)
    fun getActivityComponent(module: ModuleActivity): ComponentActivity
}