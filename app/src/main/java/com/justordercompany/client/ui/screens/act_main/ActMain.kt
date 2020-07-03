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
import com.justordercompany.client.ui.screens.act_main.tabs.list.TabList
import com.justordercompany.client.ui.screens.act_main.tabs.map.TabMap
import com.justordercompany.client.ui.screens.act_main.tabs.profile.TabProfile
import kotlinx.android.synthetic.main.act_main.*

class ActMain : BaseActivity()
{
    lateinit var vm_act_main: VmActMain
    lateinit var bnd_act_main: ActMainBinding

    lateinit var tab_profile: TabProfile
    lateinit var tab_map: TabMap
    lateinit var tab_list: TabList

    val adapter_vp_base = AdapterVpBase()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_main = DataBindingUtil.setContentView(this, R.layout.act_main)
        getActivityComponent().inject(this)
        vm_act_main = my_vm_factory.getViewModel(VmActMain::class.java)
        setBaseVmActions(vm_act_main)

        setPager()
        setListeners()
        setEvents()
    }

    private fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.orange)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.white)
        is_light_nav_bar = false
    }

    fun setPager()
    {
        bnd_act_main.vpMain.adapter = adapter_vp_base
        bnd_act_main.vpMain.offscreenPageLimit = 3

        tab_profile = TabProfile(this)
        tab_list = TabList(this)
        tab_map = TabMap(this)

        val views = arrayListOf(tab_profile.getView(), tab_map.getView(), tab_list.getView())
        adapter_vp_base.setViews(views)
    }

    fun setListeners()
    {
        bnd_act_main.lalBottomNav.lalProfile.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.PROFILE)
            })

        bnd_act_main.lalBottomNav.lalList.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.LIST)
            })

        bnd_act_main.lalBottomNav.lalMap.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedTab(TypeTab.MAP)
            })
    }

    fun setEvents()
    {
        vm_act_main.ps_scroll_to_tab.subscribe(
            {
                changeColorsOfTabs(it)
                vp_main.setCurrentItem(it.getPos(), true)
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