package com.justordercompany.client.ui.screens.act_auth

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.databinding.ActAuthBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.ui.custom_views.FlipCardAnimation

class ActAuth : BaseActivity()
{
    enum class Mode
    {
        PHONE,
        CODE
    }

    lateinit var bnd_act_auth: ActAuthBinding
    lateinit var vm_auth: VmActAuth
    lateinit var listener: ActAuthListener

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_auth = DataBindingUtil.setContentView(this, R.layout.act_auth)
        getActivityComponent().inject(this)
        vm_auth = my_vm_factory.getViewModel(VmActAuth::class.java)
        setBaseVmActions(vm_auth)
        listener = vm_auth.ViewListener()

        setListeners()
        setEvents()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.gray8_trans_50)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.gray8_trans_50)
        is_light_nav_bar = true
    }

    fun setListeners()
    {
        addBackClick(bnd_act_auth.tvTimes, vm_auth)

        bnd_act_auth.tvSend.setOnClickListener(
            {
                listener.clickedSend()
            })
    }

    fun setEvents()
    {
        vm_auth.bs_mode
                .mainThreaded()
                .subscribe(
                    {
                        animFlip(it)
                    })
                .disposeBy(composite_diposable)
    }

    private fun animFlip(mode: Mode)
    {
        val center_y = bnd_act_auth.lalAnim.height / 2f
        val center_x = bnd_act_auth.lalAnim.width / 2f
        val animation = FlipCardAnimation(0f, 180f, center_x, center_y)
        animation.setDuration(250)
        animation.setFillAfter(false)

        animation.setOnContentChangeListener(
            {
                if(mode == Mode.PHONE)
                {
                    bnd_act_auth.laPhone.visibility = View.VISIBLE
                    bnd_act_auth.laCode.visibility = View.GONE
                }
                else
                {
                    bnd_act_auth.laPhone.visibility = View.GONE
                    bnd_act_auth.laCode.visibility = View.VISIBLE
                }
            })

        bnd_act_auth.lalAnim.startAnimation(animation)
    }

    override fun finish()
    {
        super.finish()
        overridePendingTransition(R.anim.anim_fade_out, R.anim.anim_fade_in);
    }
}