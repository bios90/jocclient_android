package com.justordercompany.client.ui.screens.act_favorites

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.adapters.AdapterRvCafe
import com.justordercompany.client.databinding.ActFavoritesDialogBinding
import com.justordercompany.client.databinding.ActFilterBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.ui.screens.act_filter.VmActFilter

class ActFavorites : BaseActivity()
{
    lateinit var vm_favorites: VmActFavorites
    lateinit var bnd_act_favorites: ActFavoritesDialogBinding
    lateinit var adapter: AdapterRvCafe


    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_favorites = DataBindingUtil.setContentView(this, R.layout.act_favorites_dialog)
        getActivityComponent().inject(this)
        vm_favorites = my_vm_factory.getViewModel(VmActFavorites::class.java)
        setBaseVmActions(vm_favorites)
        setMargins()
        setRecycler()
        setEvents()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_nav_bar = true }

    private fun setMargins()
    {
        val margin_top = getStatusBarHeight() + dp2px(24f)
        val margint_bottom = getNavbarHeight() + dp2px(24f)
        val margin_side = dp2pxInt(24f)

        bnd_act_favorites.larRoot.setMargins(margin_side, margin_top.toInt(), margin_side, margint_bottom.toInt())
    }

    private fun setRecycler()
    {
        adapter = AdapterRvCafe()
        bnd_act_favorites.rvCafes.adapter = adapter
        bnd_act_favorites.rvCafes.layoutManager = LinearLayoutManager(this)
        bnd_act_favorites.rvCafes.addDivider(getColorMy(R.color.transparent), dp2pxInt(6f))
        bnd_act_favorites.rvCafes.setPadding(dp2pxInt(12), dp2pxInt(12), dp2pxInt(12), dp2pxInt(12))

        adapter.listener =
                {
                    vm_favorites.clickedCafe(it)
                }
    }

    private fun setEvents()
    {
        vm_favorites.bs_cafes
                .mainThreaded()
                .subscribe(
                    {
                        adapter.setItems(it)
                    })
                .disposeBy(composite_diposable)

        vm_favorites.bs_info_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_favorites.tvText.text = it.value
                    })
                .disposeBy(composite_diposable)

    }

}