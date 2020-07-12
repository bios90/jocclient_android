package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket

import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaBasketBinding
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabBasket(val act_cafe_menu: ActCafeMenu) : TabView
{
    val bnd_basket: LaBasketBinding
    val vm_basket: VmBasket

    init
    {
        bnd_basket = DataBindingUtil.inflate(act_cafe_menu.layoutInflater, R.layout.la_basket, null, false)
        vm_basket = act_cafe_menu.my_vm_factory.getViewModel(VmBasket::class.java)
        act_cafe_menu.setBaseVmActions(vm_basket)
    }

    override fun getView(): View
    {
        return bnd_basket.root
    }
}