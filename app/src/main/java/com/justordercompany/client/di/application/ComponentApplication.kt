package com.justordercompany.client.di.application

import com.justordercompany.client.di.activity.ComponentActivity
import com.justordercompany.client.di.activity.ModuleActivity
import com.justordercompany.client.ui.act_main.VmActMain
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleNetworking::class, ModuleGlobal::class])
interface ComponentApplication
{
    fun inject(vm: VmActMain)
    fun getActivityComponent(module: ModuleActivity): ComponentActivity
}