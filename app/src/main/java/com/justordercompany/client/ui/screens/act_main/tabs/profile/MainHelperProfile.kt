package com.justordercompany.client.ui.screens.act_main.tabs.profile

import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainProfileBinding
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.MainHelper

class MainHelperProfile(act_main: ActMain) : MainHelper
{
    val bnd_profile: LaMainProfileBinding
    val vm_main_progfile: VmMainProfile

    init
    {
        bnd_profile = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_profile, null, false)
        vm_main_progfile = act_main.my_vm_factory.getViewModel(VmMainProfile::class.java)
        act_main.setBaseVmActions(vm_main_progfile)
        setListeners()
    }

    fun setListeners()
    {
        Log.e("TestClick", "Setting listener!!")
        bnd_profile.btnAuth.setOnClickListener(
            {
                Log.e("TestClick", "Clicked!!")
                vm_main_progfile.clickedMakeAuth()
            })
    }

    override fun getView(): View
    {
        return bnd_profile.root
    }
}