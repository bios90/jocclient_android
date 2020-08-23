package com.justordercompany.client.di.application

import com.justordercompany.client.di.activity.ComponentActivity
import com.justordercompany.client.di.activity.ModuleActivity
import com.justordercompany.client.logic.utils.NotificationManager
import com.justordercompany.client.networking.FbMessagingService
import com.justordercompany.client.ui.screens.act_auth.VmActAuth
import com.justordercompany.client.ui.screens.act_cafe_menu.VmActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket.VmTabBasket
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page.VmTabCafePage
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_categ.VmTabCateg
import com.justordercompany.client.ui.screens.act_cafe_popup.VmCafePopup
import com.justordercompany.client.ui.screens.act_filter.VmActFilter
import com.justordercompany.client.ui.screens.act_main.VmActMain
import com.justordercompany.client.ui.screens.act_main.tabs.list.VmTabList
import com.justordercompany.client.ui.screens.act_main.tabs.map.VmTabMap
import com.justordercompany.client.ui.screens.act_main.tabs.profile.VmTabProfile
import com.justordercompany.client.ui.screens.act_order_dialog.VmActOrderDialog
import com.justordercompany.client.ui.screens.act_pay_type_dialog.VmPayTypeDialog
import com.justordercompany.client.ui.screens.act_product_setting.VmActProductSetting
import com.justordercompany.client.ui.screens.act_profile_edit.VmActProfileEdit
import com.justordercompany.client.ui.screens.act_review_dialog.VmActReviewDialog
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleNetworking::class, ModuleGlobal::class])
interface ComponentApplication
{
    fun inject(fb_messaging_service: FbMessagingService)
    fun inject(notification_manager: NotificationManager)
    fun getActivityComponent(module: ModuleActivity): ComponentActivity
    fun inject(vm: VmActMain)
    fun inject(vm: VmTabMap)
    fun inject(vm: VmTabProfile)
    fun inject(vm: VmActProfileEdit)
    fun inject(vm: VmActFilter)
    fun inject(vm: VmCafePopup)
    fun inject(vm: VmActCafeMenu)
    fun inject(vm: VmTabCafePage)
    fun inject(vm: VmTabBasket)
    fun inject(vm: VmTabList)
    fun inject(vm: VmTabCateg)
    fun inject(vm: VmActProductSetting)
    fun inject(vm: VmActOrderDialog)
    fun inject(vm: VmPayTypeDialog)
    fun inject(vm: VmActReviewDialog)
    fun inject(vm: VmActAuth)
}