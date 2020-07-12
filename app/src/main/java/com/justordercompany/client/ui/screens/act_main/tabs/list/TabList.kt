package com.justordercompany.client.ui.screens.act_main.tabs.list

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.RecyclerLoadInfo
import com.justordercompany.client.base.adapters.AdapterRvCafe
import com.justordercompany.client.base.adapters.AdapterVpBase
import com.justordercompany.client.databinding.LaMainListBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.dp2pxInt
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabList(val act_main: ActMain) : TabView
{
    val bnd_list: LaMainListBinding
    lateinit var rec_by_distance: RecyclerView
    lateinit var rec_by_rating: RecyclerView
    val adapter: AdapterVpBase = AdapterVpBase()
    val adapter_rec_distance = AdapterRvCafe()
    val adapter_rec_rating = AdapterRvCafe()
    val vm_tab_list: VmTabList

    init
    {
        bnd_list = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_list, null, false)
        vm_tab_list = act_main.my_vm_factory.getViewModel(VmTabList::class.java)
        setAdapters()
        setEvents()
    }

    fun setEvents()
    {
        vm_tab_list.bs_current_cafes
                .mainThreaded()
                .subscribe(
                    {
                        Log.e("TabList", "setEvents: Hot Here!!")

                        val by_rating = it.items.sortedBy({ it.rating })
                        val by_distance = it.items.sortedBy({ it.distance })

                        adapter_rec_rating.setItems(RecyclerLoadInfo(by_rating,it.load_behavior))
                        adapter_rec_distance.setItems(RecyclerLoadInfo(by_distance,it.load_behavior))
                    })
                .disposeBy(act_main.composite_diposable)
    }

    private fun setAdapters()
    {
        bnd_list.vpLists.adapter = adapter
        bnd_list.vpLists.offscreenPageLimit = 2

        rec_by_distance = RecyclerView(act_main)
        rec_by_distance.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        rec_by_distance.adapter = adapter_rec_distance
        rec_by_distance.layoutManager = LinearLayoutManager(act_main)
        rec_by_distance.setPadding(dp2pxInt(12), dp2pxInt(12), dp2pxInt(12), dp2pxInt(12))
        rec_by_distance.clipToPadding = false
        rec_by_distance.clipChildren = false

        rec_by_rating = RecyclerView(act_main)
        rec_by_rating.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        rec_by_rating.adapter = adapter_rec_rating
        rec_by_rating.layoutManager = LinearLayoutManager(act_main)
        rec_by_rating.setPadding(dp2pxInt(12), dp2pxInt(12), dp2pxInt(12), dp2pxInt(12))
        rec_by_rating.clipToPadding = false
        rec_by_rating.clipChildren = false


        adapter.setViews(arrayListOf(rec_by_distance, rec_by_rating))
        bnd_list.tabs.setViewPager(bnd_list.vpLists)
    }

    override fun getView(): View
    {
        return bnd_list.root
    }

}