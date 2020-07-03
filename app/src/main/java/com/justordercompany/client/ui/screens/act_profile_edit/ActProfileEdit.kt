package com.justordercompany.client.ui.screens.act_profile_edit

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.databinding.ActProfileEditBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.logic.utils.images.GlideManager

class ActProfileEdit : BaseActivity()
{
    lateinit var vm_act_profile__edit: VmActProfileEdit
    lateinit var bnd_act_profile_edit: ActProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_profile_edit = DataBindingUtil.setContentView(this, R.layout.act_profile_edit)
        getActivityComponent().inject(this)
        vm_act_profile__edit = my_vm_factory.getViewModel(VmActProfileEdit::class.java)
        setBaseVmActions(vm_act_profile__edit)

        setEvents()
        setListeners()
    }

    private fun setNavStatus()
    {

    }

    private fun setListeners()
    {
        bnd_act_profile_edit.imgAvatar.setOnClickListener(
            {
                vm_act_profile__edit.ViewListener().clickedImage()
            })
    }

    private fun setEvents()
    {
        vm_act_profile__edit.bs_image
                .mainThreaded()
                .subscribe(
                    {
                        val uri = it.value?.image_url ?: return@subscribe

                        GlideManager.loadImageSimple(uri, bnd_act_profile_edit.imgAvatar)
                    })
                .disposeBy(composite_diposable)
    }
}