package com.justordercompany.client.ui.screens.act_main.tabs.map

import com.justordercompany.client.logic.models.ModelCafe

interface TabMapListener
{
    fun mapInited()
    fun mapIdled()
    fun clickedCafe(cafe: ModelCafe)
    fun clickedLegend()
}