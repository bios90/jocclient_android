package com.justordercompany.client.ui.screens.act_cafe_popup

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.databinding.ActCafePopupBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.images.GlideManager
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import java.lang.RuntimeException
import android.view.WindowManager
import android.view.Gravity



class ActCafePopup : BaseActivity()
{
    lateinit var vm_cafe_popup: VmCafePopup
    lateinit var bnd_act_popup: ActCafePopupBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        applySliderBottomWithUpadte()
        bnd_act_popup = DataBindingUtil.setContentView(this, R.layout.act_cafe_popup)
        getActivityComponent().inject(this)
        vm_cafe_popup = my_vm_factory.getViewModel(VmCafePopup::class.java)
        setEvents()
        setListeners()
        setBaseVmActions(vm_cafe_popup)
        checkExtra()
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
        overlays.add(bnd_act_popup.lalOverlay)
    }

    private fun setEvents()
    {
        vm_cafe_popup.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        bindCafe(it)
                    })
                .disposeBy(composite_diposable)

        vm_cafe_popup.ps_cafe_distance_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_popup.tvDistance.text = it
                    })
                .disposeBy(composite_diposable)
    }

    private fun bindCafe(cafe: ModelCafe)
    {
        cafe.logo?.url_l?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_act_popup.imgLogo)
            })
        bnd_act_popup.tvName.text = cafe.name
        bnd_act_popup.tvAdress.text = cafe.address

        cafe.rating?.let(
            {
                bnd_act_popup.ratingBar.rating = it
            })

        bnd_act_popup.tvAbout.text = "aksfbjkafhb jkdfhbgjhdfbla lsadbflsa bhsbdf jahksbjhblhabfgk"

        bnd_act_popup.tvTime.text = "25:00 - 26:00"
    }

    private fun checkExtra()
    {
        val cafe_id = intent.getIntExtraMy(Constants.Extras.EXTRA_CAFE_ID)
        if (cafe_id == null)
        {
            throw RuntimeException("**** Errr no cafe id passed ****")
        }
        vm_cafe_popup.bs_cafe_id.onNext(cafe_id)
    }
}