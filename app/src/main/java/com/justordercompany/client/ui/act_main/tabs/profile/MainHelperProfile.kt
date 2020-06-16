package com.justordercompany.client.ui.act_main.tabs.profile

import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainListBinding
import com.justordercompany.client.databinding.LaMainProfileBinding
import com.justordercompany.client.ui.act_main.ActMain
import com.justordercompany.client.ui.act_main.tabs.MainHelper

class MainHelperProfile(act_main:ActMain):MainHelper
{
    val bnd_profile: LaMainProfileBinding

    init
    {
        bnd_profile = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_profile, null, false)
    }

    override fun getView(): View
    {
        return bnd_profile.root
    }
}