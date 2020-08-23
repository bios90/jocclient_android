package com.justordercompany.client.ui.screens.act_auth

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass.Companion.composite_disposable
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.enums.TypeAuthMode
import com.justordercompany.client.databinding.ActAuthBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.utils.strings.getOffertText

class ActAuth : BaseActivity()
{
    lateinit var bnd_act_auth: ActAuthBinding
    lateinit var vm_act_auth: VmActAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        bnd_act_auth = DataBindingUtil.setContentView(this, R.layout.act_auth)
        vm_act_auth = my_vm_factory.getViewModel(VmActAuth::class.java)
        setBaseVmActions(vm_act_auth)

        setEvents()
        setListeners()
        bnd_act_auth.tvOffert.text = getOffertText()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        is_light_nav_bar = true
    }

    fun setListeners()
    {
        bnd_act_auth.tvSend.setOnClickListener(
            {
                vm_act_auth.ViewListener().clickedAuthLogin()
            })

        bnd_act_auth.tvOffert.setOnClickListener(
            {
                vm_act_auth.ViewListener().clickedOffertRules()
            })

        connectBoth(bnd_act_auth.etPhone.getBsText(), vm_act_auth.bs_phone, composite_diposable)
        connectBoth(bnd_act_auth.etCode.getBsText(), vm_act_auth.bs_code, composite_diposable)
        connectBoth(bnd_act_auth.chOffert.getBs(), vm_act_auth.bs_offert_checked, composite_diposable)
    }

    fun setEvents()
    {
        vm_act_auth.bs_auth_mode.subscribe(
            { mode ->

                val action =
                        {
                            if (mode == TypeAuthMode.CODE)
                            {
                                bnd_act_auth.laCode.visibility = View.VISIBLE
                                bnd_act_auth.laPhone.visibility = View.GONE
                                bnd_act_auth.tvSend.text = getStringMy(R.string.confirm)
                            }
                            else
                            {
                                bnd_act_auth.laCode.visibility = View.GONE
                                bnd_act_auth.laPhone.visibility = View.VISIBLE
                                bnd_act_auth.tvSend.text = getStringMy(R.string.send_code)
                            }
                        }

                bnd_act_auth.lalAnim.animateFlip(action)
            })
                .disposeBy(composite_disposable)
    }
}