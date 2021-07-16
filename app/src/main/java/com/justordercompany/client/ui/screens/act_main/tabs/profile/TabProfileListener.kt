package com.justordercompany.client.ui.screens.act_main.tabs.profile

import com.justordercompany.client.logic.models.ModelOrder

interface TabProfileListener
{
    fun clickedAuthLogin()
    fun clickedOffertRules()
    fun clickedEditUser()
    fun clickedQuestion()
    fun swipedToRefresh()
    fun scrolledToBottom()
    fun clickedOrder(order:ModelOrder)
    fun clickedFavorites()
}