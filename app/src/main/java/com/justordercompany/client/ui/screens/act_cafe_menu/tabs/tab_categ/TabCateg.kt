package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_categ

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.justordercompany.client.R
import com.justordercompany.client.base.adapters.AdapterRvCafe
import com.justordercompany.client.base.adapters.AdapterRvProducts
import com.justordercompany.client.databinding.LaTabCategBinding
import com.justordercompany.client.extensions.addDivider
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.dp2pxInt
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabCateg(val act_cafe_menu: ActCafeMenu) : TabView
{
    val bnd_categ: LaTabCategBinding
    val vm_tab_categ: VmTabCateg
    val composite_disposable = act_cafe_menu.composite_diposable
    val adapter = AdapterRvProducts()

    init
    {
        bnd_categ = DataBindingUtil.inflate(act_cafe_menu.layoutInflater, R.layout.la_tab_categ, null, false)
        vm_tab_categ = VmTabCateg()
        act_cafe_menu.setBaseVmActions(vm_tab_categ)
        setRecycler()
        setEvents()
    }

    override fun getView(): View
    {
        return bnd_categ.root
    }

    fun setRecycler()
    {
        bnd_categ.rec.adapter = adapter
        bnd_categ.rec.layoutManager = LinearLayoutManager(act_cafe_menu)
        bnd_categ.rec.addDivider(getColorMy(R.color.transparent), dp2pxInt(6f))
        adapter.action_card_clicked =
                {
                    vm_tab_categ.ViewListener().clickedProduct(it)
                }
    }

    private fun setEvents()
    {
        vm_tab_categ.bs_products_to_display
                .subscribe(
                    {
                        adapter.setItems(it)
                    })
                .disposeBy(composite_disposable)

        vm_tab_categ.bs_type
                .subscribe(
                    {
                        bnd_categ.tvHeader.text = it.getNameForHeader()
                    })
                .disposeBy(composite_disposable)
    }
}