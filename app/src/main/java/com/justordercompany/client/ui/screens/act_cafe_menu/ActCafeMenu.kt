package com.justordercompany.client.ui.screens.act_cafe_menu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.adapters.AdapterVpBase
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.base.enums.TypeTabCafe
import com.justordercompany.client.databinding.ActCafeMenuBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.hasColdDrinks
import com.justordercompany.client.logic.models.hasHotDrinks
import com.justordercompany.client.logic.models.hasSnacks
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket.TabBasket
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page.TabCafePage
import com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_categ.TabCateg
import java.lang.RuntimeException

class ActCafeMenu : BaseActivity()
{
    lateinit var vm_act_cafe_menu: VmActCafeMenu
    lateinit var bnd_act_cafe_menu: ActCafeMenuBinding

    val adapter_vp_base = AdapterVpBase()
    lateinit var tab_cafe_page: TabCafePage
    lateinit var tab_basket: TabBasket
    var tab_hot: TabCateg? = null
    var tab_cold: TabCateg? = null
    var tab_snacks: TabCateg? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_cafe_menu = DataBindingUtil.setContentView(this, R.layout.act_cafe_menu)
        getActivityComponent().inject(this)
        vm_act_cafe_menu = my_vm_factory.getViewModel(VmActCafeMenu::class.java)
        setBaseVmActions(vm_act_cafe_menu)

        applyStatusSizeAndColor()
        checkExtra()
        setEvents()
        setListeners()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
    }

    fun applyStatusSizeAndColor()
    {
        val height = getStatusBarHeight()
        bnd_act_cafe_menu.laFakeStatus.setHeight(height)
    }

    fun setEvents()
    {
        vm_act_cafe_menu.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        setPager(it)
                        tab_cafe_page.vm_tab_cafe_page.bs_cafe.onNext(it)
                    })
                .disposeBy(composite_diposable)

        vm_act_cafe_menu.bs_current_tab
                .mainThreaded()
                .subscribe(
                    {
                        val cafe = vm_act_cafe_menu.bs_cafe.value ?: return@subscribe
                        val pos = it.getPos(cafe) ?: return@subscribe

                        bnd_act_cafe_menu.vpCafe.setCurrentItem(pos, true)
                        changeColorsOfTabs(it)
                    })
                .disposeBy(composite_diposable)

        BasketManager.bs_items
                .subscribe(
                    {
                        val count = it.size
                        bnd_act_cafe_menu.lalBottomNav.tvBasketCount.text = count.toString()
                    })
                .disposeBy(composite_diposable)
    }

    private fun setPager(cafe: ModelCafe)
    {
        Log.e("ActCafeMenu", "setPager: Set pager!!!")
        bnd_act_cafe_menu.vpCafe.adapter = adapter_vp_base
        bnd_act_cafe_menu.vpCafe.offscreenPageLimit = 10

        val tab_views: ArrayList<View> = arrayListOf()

        tab_cafe_page = TabCafePage(this)
        tab_views.add(tab_cafe_page.getView())

        if (cafe.hasHotDrinks())
        {
            tab_hot = TabCateg(this)
            tab_hot!!.vm_tab_categ.bs_type.onNext(TypeProduct.HOT)
            tab_hot!!.vm_tab_categ.bs_cafe.onNext(cafe)
            tab_views.add(tab_hot!!.getView())
            bnd_act_cafe_menu.lalBottomNav.lalHotDrinks.visibility = View.VISIBLE
        }

        if (cafe.hasColdDrinks())
        {
            tab_cold = TabCateg(this)
            tab_cold!!.vm_tab_categ.bs_type.onNext(TypeProduct.COLD)
            tab_cold!!.vm_tab_categ.bs_cafe.onNext(cafe)
            tab_views.add(tab_cold!!.getView())
            bnd_act_cafe_menu.lalBottomNav.lalColdDrinks.visibility = View.VISIBLE
        }

        if (cafe.hasSnacks())
        {
            tab_snacks = TabCateg(this)
            tab_snacks!!.vm_tab_categ.bs_type.onNext(TypeProduct.SNACK)
            tab_snacks!!.vm_tab_categ.bs_cafe.onNext(cafe)
            tab_views.add(tab_snacks!!.getView())
            bnd_act_cafe_menu.lalBottomNav.lalSnacks.visibility = View.VISIBLE
        }

        tab_basket = TabBasket(this)
        tab_views.add(tab_basket.getView())

        adapter_vp_base.setViews(tab_views)
        bnd_act_cafe_menu.vpCafe.setCurrentItem(1,false)
    }

    private fun setListeners()
    {
        bnd_act_cafe_menu.vpCafe.addOnPageChangeListener(object:ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int)
            {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                val cafe = vm_act_cafe_menu.bs_cafe.value ?: return
                val type = TypeTabCafe.initFromPagerPos(position,cafe)
                vm_act_cafe_menu.bs_current_tab.onNext(type)
            }
        })

        bnd_act_cafe_menu.lalBottomNav.lalCafeInfo.setOnClickListener(
            {
                vm_act_cafe_menu.bs_current_tab.onNext(TypeTabCafe.CAFE)
            })

        bnd_act_cafe_menu.lalBottomNav.lalHotDrinks.setOnClickListener(
            {
                vm_act_cafe_menu.bs_current_tab.onNext(TypeTabCafe.HOT)
            })

        bnd_act_cafe_menu.lalBottomNav.lalColdDrinks.setOnClickListener(
            {
                vm_act_cafe_menu.bs_current_tab.onNext(TypeTabCafe.COLD)
            })

        bnd_act_cafe_menu.lalBottomNav.lalSnacks.setOnClickListener(
            {
                vm_act_cafe_menu.bs_current_tab.onNext(TypeTabCafe.SNACKS)
            })

        bnd_act_cafe_menu.lalBottomNav.lalBasket.setOnClickListener(
            {
                vm_act_cafe_menu.bs_current_tab.onNext(TypeTabCafe.BASKET)
            })
    }

    private fun changeColorsOfTabs(tab: TypeTabCafe)
    {
        val all_tabs_count = bnd_act_cafe_menu.lalBottomNav.lalRoot.childCount
        val colors_grad = arrayListOf(getColorMy(R.color.orange_dark), getColorMy(R.color.orange_light))
        val color_gray_5 = getColorMy(R.color.gray5)

        for (i in 0 until all_tabs_count)
        {
            val lal = bnd_act_cafe_menu.lalBottomNav.lalRoot.getChildAt(i) as ViewGroup
            val tv = lal.getChildAt(0) as TextView
            if (tab.getPosInBottomNav() == i)
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

    private fun checkExtra()
    {
        val cafe_id = intent.getIntExtraMy(Constants.Extras.EXTRA_CAFE_ID)
        if (cafe_id == null)
        {
            throw RuntimeException("**** Errr no cafe id passed ****")
        }
        vm_act_cafe_menu.bs_cafe_id.onNext(cafe_id)

        val order_id = intent.getIntExtraMy(Constants.Extras.EXTRA_ORDER_ID)
        if(order_id != null)
        {
            vm_act_cafe_menu.bs_order_id.onNext(order_id)
        }
    }
}