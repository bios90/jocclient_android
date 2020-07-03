package com.justordercompany.client.ui.screens.act_main.tabs.list

import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainListBinding
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.ActMainTab

class TabList(val act_main:ActMain): ActMainTab
{
    val bnd_list:LaMainListBinding

    init
    {
        bnd_list =  DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_list,null,false)
    }

    override fun getView(): View
    {
        return bnd_list.root
    }
}