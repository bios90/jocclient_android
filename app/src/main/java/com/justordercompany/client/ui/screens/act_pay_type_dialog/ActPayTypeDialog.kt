package com.justordercompany.client.ui.screens.act_pay_type_dialog

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.databinding.ActPayTypeDialogBinding
import com.justordercompany.client.extensions.getColorMy

class ActPayTypeDialog : BaseActivity()
{
    lateinit var bnd_pay_type_dialog: ActPayTypeDialogBinding
    lateinit var vm_pay_type: VmPayTypeDialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        applySliderBottom()
        super.onCreate(savedInstanceState)
        bnd_pay_type_dialog = DataBindingUtil.setContentView(this, R.layout.act_pay_type_dialog)
        getActivityComponent().inject(this)
        vm_pay_type = my_vm_factory.getViewModel(VmPayTypeDialog::class.java)
        setBaseVmActions(vm_pay_type)

        setEvents()
        setListeners()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.white)
        is_light_nav_bar = false
    }

    private fun setListeners()
    {
        bnd_pay_type_dialog.lalCreditCard.setOnClickListener(
            {
                vm_pay_type.ViewListener().clickedNewCard()
            })

        bnd_pay_type_dialog.lalGooglePay.setOnClickListener(
            {
                vm_pay_type.ViewListener().clickedGooglePlay()
            })
    }

    private fun setEvents()
    {
    }
}