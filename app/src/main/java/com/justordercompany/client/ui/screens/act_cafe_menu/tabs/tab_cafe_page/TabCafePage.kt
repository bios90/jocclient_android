package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import android.view.View
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaCafePageBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabCafePage(val act_cafe_menu: ActCafeMenu) : TabView
{
    val bnd_cafe_page: LaCafePageBinding
    val vm_cafe_page: VmCafePage
    val composite_disposable = act_cafe_menu.composite_diposable

    init
    {
        bnd_cafe_page = DataBindingUtil.inflate(act_cafe_menu.layoutInflater, R.layout.la_cafe_page, null, false)
        vm_cafe_page = act_cafe_menu.my_vm_factory.getViewModel(VmCafePage::class.java)
        act_cafe_menu.setBaseVmActions(vm_cafe_page)

        setEvents()
    }

    override fun getView(): View
    {
        return bnd_cafe_page.root
    }

    fun setEvents()
    {
        vm_cafe_page.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        bindCafe(it)
                    })
                .disposeBy(composite_disposable)

        vm_cafe_page.ps_cafe_distance_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_cafe_page.tvDistance.text = it
                    })
                .disposeBy(composite_disposable)
    }

    private fun bindCafe(cafe: ModelCafe)
    {
        bnd_cafe_page.tvName.text = cafe.name
        cafe.rating?.let(
            {
                bnd_cafe_page.ratingBar.rating = it
            })
        cafe.logo?.url_l?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_cafe_page.imgLogo)
            })

        bnd_cafe_page.ftvText.setTypeface(getTypeFaceFromResource(R.font.exo_reg))
        bnd_cafe_page.ftvText.setTextSize(getDimenMy(R.dimen.size_s))
        bnd_cafe_page.ftvText.setTextColor(getColorMy(R.color.white))
        bnd_cafe_page.ftvText.setText("\n\n\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.")

        //Todo make cafe images binding here

        /////

        bnd_cafe_page.tvAdress.text = cafe.address
        bnd_cafe_page.tvTime.text = "25:00 - 39:00"
    }
}
















