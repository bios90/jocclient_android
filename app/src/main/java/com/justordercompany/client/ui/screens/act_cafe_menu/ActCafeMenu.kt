package com.justordercompany.client.ui.screens.act_cafe_menu

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.adapters.AdapterVpBase
import com.justordercompany.client.databinding.ActCafeMenuBinding
import com.justordercompany.client.databinding.ActCafePopupBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getIntExtraMy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket.TabBasket
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page.TabCafePage
import java.lang.RuntimeException

class ActCafeMenu : BaseActivity()
{
    lateinit var vm_act_cafe_menu: VmActCafeMenu
    lateinit var bnd_act_cafe_menu: ActCafeMenuBinding

    val adapter_vp_base = AdapterVpBase()
    lateinit var tab_cafe_page: TabCafePage
    lateinit var tab_basket: TabBasket

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_cafe_menu = DataBindingUtil.setContentView(this, R.layout.act_cafe_menu)
        getActivityComponent().inject(this)
        vm_act_cafe_menu = my_vm_factory.getViewModel(VmActCafeMenu::class.java)
        setBaseVmActions(vm_act_cafe_menu)

        checkExtra()
        setEvents()
    }

    fun setNavStatus()
    {

    }

    fun setEvents()
    {
        vm_act_cafe_menu.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        setPager(it)
                        tab_cafe_page.vm_cafe_page.bs_cafe.onNext(it)
                    })
                .disposeBy(composite_diposable)
    }

    private fun setPager(cafe: ModelCafe)
    {
        Log.e("ActCafeMenu", "setPager: Got here!!!")
        bnd_act_cafe_menu.vpCafe.adapter = adapter_vp_base
        bnd_act_cafe_menu.vpCafe.offscreenPageLimit = 10

        tab_cafe_page = TabCafePage(this)
        tab_basket = TabBasket(this)

        val views = arrayListOf(tab_cafe_page.getView(), tab_basket.getView())
        adapter_vp_base.setViews(views)
    }

    private fun checkExtra()
    {
        val cafe_id = intent.getIntExtraMy(Constants.Extras.EXTRA_CAFE_ID)
        if (cafe_id == null)
        {
            throw RuntimeException("**** Errr no cafe id passed ****")
        }
        vm_act_cafe_menu.bs_cafe_id.onNext(cafe_id)
    }
}