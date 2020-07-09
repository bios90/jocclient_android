package com.justordercompany.client.ui.screens.act_profile_edit

import android.os.Bundle
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.databinding.ActProfileEditBinding
import com.justordercompany.client.extensions.*
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
        setEvents()
        setListeners()
        setBaseVmActions(vm_act_profile__edit)

    }

    private fun setNavStatus()
    {
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.gray8).overlay(getColorMy(R.color.white))
    }

    private fun setListeners()
    {
        bnd_act_profile_edit.imgAvatar.setOnClickListener(
            {
                vm_act_profile__edit.ViewListener().clickedImage()
            })

        bnd_act_profile_edit.tvSend.setOnClickListener(
            {
                vm_act_profile__edit.ViewListener().clickedSave()
            })

        connectBoth(bnd_act_profile_edit.etName.getBsText(), vm_act_profile__edit.bs_name, composite_diposable)
        connectBoth(bnd_act_profile_edit.etEmail.getBsText(), vm_act_profile__edit.bs_email, composite_diposable)
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

        vm_act_profile__edit.bs_user_to_display
                .mainThreaded()
                .subscribe(
                    {
                        val user = it.value ?: return@subscribe

                        bnd_act_profile_edit.etEmail.setText(user.email)
                        bnd_act_profile_edit.etPhone.setText(user.phone)
                        user.image?.url_l?.let(
                            {
                                GlideManager.loadImageSimple(it, bnd_act_profile_edit.imgAvatar)
                            })
                    })
                .disposeBy(composite_diposable)
    }
}