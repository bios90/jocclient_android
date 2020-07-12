package com.justordercompany.client.di.activity

import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_popup.ActCafePopup
import com.justordercompany.client.ui.screens.act_filter.ActFilter
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_profile_edit.ActProfileEdit
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act_main:ActMain)
    fun inject(act_main:ActProfileEdit)
    fun inject(act_main:ActFilter)
    fun inject(act_main:ActCafePopup)
    fun inject(act_main:ActCafeMenu)
}