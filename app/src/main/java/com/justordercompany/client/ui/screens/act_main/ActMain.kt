package com.justordercompany.client.ui.screens.act_main

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.adapters.AdapterVpBase
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.databinding.ActMainBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.makeTextGradient
import com.justordercompany.client.extensions.removeGradient
import com.justordercompany.client.ui.screens.act_main.tabs.list.MainHelperList
import com.justordercompany.client.ui.screens.act_main.tabs.map.MainHelperMap
import com.justordercompany.client.ui.screens.act_main.tabs.profile.MainHelperProfile
import kotlinx.android.synthetic.main.act_main.*

class ActMain : BaseActivity()
{
    lateinit var vm_act_main: VmActMain
    lateinit var bnd_act_main: ActMainBinding
    lateinit var listener: ActMainListener

    lateinit var main_helper_profile: MainHelperProfile
    lateinit var main_helper_map: MainHelperMap
    lateinit var main_helper_list: MainHelperList

    val adapter_vp_base = AdapterVpBase()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        color_status_bar = getColorMy(R.color.orange)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.white)
        is_light_nav_bar = false
        super.onCreate(savedInstanceState)
        bnd_act_main = DataBindingUtil.setContentView(this, R.layout.act_main)
        getActivityComponent().inject(this)
        vm_act_main = my_vm_factory.getViewModel(VmActMain::class.java)
        setBaseVmActions(vm_act_main)
        listener = vm_act_main.ViewListener()

        setPager()
        setListeners()
        setEvents()
    }

    fun setPager()
    {
        bnd_act_main.vpMain.adapter = adapter_vp_base
        bnd_act_main.vpMain.offscreenPageLimit = 3

        main_helper_profile = MainHelperProfile(this)
        main_helper_list = MainHelperList(this)
        main_helper_map = MainHelperMap(this)

        val views = arrayListOf(main_helper_profile.getView(), main_helper_map.getView(), main_helper_list.getView())
        adapter_vp_base.setViews(views)
    }

    fun setListeners()
    {
        bnd_act_main.lalBottomNav.lalProfile.setOnClickListener(
            {
                listener.clickedTab(TypeTab.PROFILE)
            })

        bnd_act_main.lalBottomNav.lalList.setOnClickListener(
            {
                listener.clickedTab(TypeTab.LIST)
            })

        bnd_act_main.lalBottomNav.lalMap.setOnClickListener(
            {
                listener.clickedTab(TypeTab.MAP)
            })
    }

    fun setEvents()
    {
        vm_act_main.ps_scroll_to_tab.subscribe(
            {
                changeColorsOfTabs(it)
                vp_main.setCurrentItem(it.getPos(),true)
            })
                .disposeBy(composite_diposable)
    }

    private fun changeColorsOfTabs(tab: TypeTab)
    {
        val tabs_count = bnd_act_main.lalBottomNav.lalRoot.childCount
        val colors_grad = arrayListOf(getColorMy(R.color.orange_dark), getColorMy(R.color.orange_light))
        val color_gray_5 = getColorMy(R.color.gray5)

        for (i in 0 until tabs_count)
        {
            val lal = bnd_act_main.lalBottomNav.lalRoot.getChildAt(i) as LinearLayout
            val tv = lal.getChildAt(0) as TextView
            if (tab.getPos() == i)
            {
                tv.makeTextGradient(colors_grad)
            }
            else
            {
                tv.removeGradient()
                tv.setTextColor(color_gray_5)
            }
        }
    }
}