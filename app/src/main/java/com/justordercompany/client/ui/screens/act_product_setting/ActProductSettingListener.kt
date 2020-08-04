package com.justordercompany.client.ui.screens.act_product_setting

import com.justordercompany.client.logic.models.ModelAddableValue

interface ActProductSettingListener
{
    fun sugarChangedTo(pos:Int)
    fun weightChangedTo(weight:ModelAddableValue)
    fun milkChangedTo(milk:ModelAddableValue)
    fun addablesChangedTo(addables:List<ModelAddableValue>)
    fun clickedAdd()
}