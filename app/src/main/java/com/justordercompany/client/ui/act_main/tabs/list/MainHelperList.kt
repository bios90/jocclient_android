package com.justordercompany.client.ui.act_main.tabs.list

import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainListBinding
import com.justordercompany.client.ui.act_main.ActMain
import com.justordercompany.client.ui.act_main.tabs.MainHelper

class MainHelperList(val act_main:ActMain): MainHelper
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