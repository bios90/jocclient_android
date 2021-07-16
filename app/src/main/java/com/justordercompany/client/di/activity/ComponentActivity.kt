package com.justordercompany.client.di.activity

import com.justordercompany.client.ui.screens.act_auth.ActAuth
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_popup.ActCafePopup
import com.justordercompany.client.ui.screens.act_favorites.ActFavorites
import com.justordercompany.client.ui.screens.act_filter.ActFilter
import com.justordercompany.client.ui.screens.act_info_dialog.ActInfoDialog
import com.justordercompany.client.ui.screens.act_intro_slides.ActIntroSlides
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_order_dialog.ActOrderDialog
import com.justordercompany.client.ui.screens.act_pay_type_dialog.ActPayTypeDialog
import com.justordercompany.client.ui.screens.act_product_setting.ActProductSetting
import com.justordercompany.client.ui.screens.act_profile_edit.ActProfileEdit
import com.justordercompany.client.ui.screens.act_review_dialog.ActReviewDialog
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act:ActMain)
    fun inject(act:ActProfileEdit)
    fun inject(act:ActFilter)
    fun inject(act:ActCafePopup)
    fun inject(act:ActCafeMenu)
    fun inject(act:ActProductSetting)
    fun inject(act:ActOrderDialog)
    fun inject(act:ActPayTypeDialog)
    fun inject(act:ActReviewDialog)
    fun inject(act:ActAuth)
    fun inject(act:ActInfoDialog)
    fun inject(act:ActIntroSlides)
    fun inject(act:ActFavorites)
}