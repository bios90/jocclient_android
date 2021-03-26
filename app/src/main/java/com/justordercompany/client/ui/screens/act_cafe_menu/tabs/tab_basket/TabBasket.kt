package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.justordercompany.client.R
import com.justordercompany.client.base.FeedDisplayInfo
import com.justordercompany.client.base.LoadBehavior
import com.justordercompany.client.base.adapters.AdapterRvBasket
import com.justordercompany.client.databinding.LaTabBasketBinding
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.tabs.TabView
import androidx.recyclerview.widget.DividerItemDecoration
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.strings.getOffertText
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator


class TabBasket(val act_cafe_menu: ActCafeMenu) : TabView
{
    val composite_disposable = act_cafe_menu.composite_diposable
    val bnd_tab_basket: LaTabBasketBinding
    val vm_tab_basket: VmTabBasket
    val adapter: AdapterRvBasket

    init
    {
        bnd_tab_basket = DataBindingUtil.inflate(act_cafe_menu.layoutInflater, R.layout.la_tab_basket, null, false)
        vm_tab_basket = act_cafe_menu.my_vm_factory.getViewModel(VmTabBasket::class.java)
        act_cafe_menu.setBaseVmActions(vm_tab_basket)
        adapter = AdapterRvBasket()

        setViews()
        setEvents()
    }

    override fun getView(): View
    {
        return bnd_tab_basket.root
    }

    fun setViews()
    {
        bnd_tab_basket.rec.adapter = adapter
        bnd_tab_basket.rec.layoutManager = LinearLayoutManager(act_cafe_menu)
        bnd_tab_basket.rec.itemAnimator = SlideInLeftAnimator()

        val drw = GradientDrawable()
        drw.shape = GradientDrawable.RECTANGLE
        drw.setSize(0, dp2pxInt(6))

        bnd_tab_basket.rec.addDivider(getColorMy(R.color.transparent), dp2pxInt(6f))

        adapter.action_clicked_edit =
                {
                    vm_tab_basket.ViewListener().clickedEdit(it)
                }

        adapter.action_clicked_delete =
                {
                    vm_tab_basket.ViewListener().clickedDelete(it)
                }

        bnd_tab_basket.tvOrder.setOnClickListener(
            {
                vm_tab_basket.ViewListener().clickedOrder()
            })

        bnd_tab_basket.tvQuickOrder.setOnClickListener(
            {
                vm_tab_basket.ViewListener().clickedQuickOrder()
            })

        bnd_tab_basket.tvRegister.setOnClickListener(
            {
                vm_tab_basket.ViewListener().clickedRegister()
            })
    }

    fun setEvents()
    {
       act_cafe_menu.vm_act_cafe_menu.bs_cafe
               .mainThreaded()
               .subscribe(
                   {
                       val can_order = it.can_order ?: false
//                       val can_order = true
                       bnd_tab_basket.lalCanOrder.visibility = can_order.toVisibility()
                       bnd_tab_basket.tvCantOrder.visibility = (!can_order).toVisibility()
                   })
               .disposeBy(composite_disposable)

        vm_tab_basket.bs_basket_items
                .subscribe(
                    {
                        val info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                        adapter.setItems(info)

                        bnd_tab_basket.tvSum.text = BasketManager.getSumText()
                    })
                .disposeBy(composite_disposable)

        vm_tab_basket.bs_show_register
                .subscribe(
                    {
                        if(it)
                        {
                            bnd_tab_basket.tvRegister.visibility = View.VISIBLE
                            bnd_tab_basket.tvRegisterTitle.visibility = View.VISIBLE
                            bnd_tab_basket.tvQuickOrder.visibility = View.GONE
                            bnd_tab_basket.tvOrder.visibility = View.GONE
                        }
                        else
                        {
                            bnd_tab_basket.tvRegister.visibility = View.GONE
                            bnd_tab_basket.tvRegisterTitle.visibility = View.GONE
                            bnd_tab_basket.tvQuickOrder.visibility = View.VISIBLE
                            bnd_tab_basket.tvOrder.visibility = View.VISIBLE
                        }
                    })
                .disposeBy(composite_disposable)


    }
}