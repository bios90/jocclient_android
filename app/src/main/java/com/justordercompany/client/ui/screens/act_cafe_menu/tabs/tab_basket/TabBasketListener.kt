package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket

import com.justordercompany.client.logic.models.ModelBasketItem

interface TabBasketListener
{
    fun clickedEdit(item:ModelBasketItem)
    fun clickedDelete(item:ModelBasketItem)
    fun clickedOrder()
    fun clickedQuickOrder()
    fun clickedRegister()
}