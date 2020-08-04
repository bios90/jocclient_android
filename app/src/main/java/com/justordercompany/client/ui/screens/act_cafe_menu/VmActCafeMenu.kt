package com.justordercompany.client.ui.screens.act_cafe_menu

import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeTabCafe
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.hasColdDrinks
import com.justordercompany.client.logic.models.hasHotDrinks
import com.justordercompany.client.logic.models.hasSnacks
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.toLatLng
import io.reactivex.subjects.BehaviorSubject
import java.lang.RuntimeException

class VmActCafeMenu : BaseViewModel()
{
    val bs_cafe_id: BehaviorSubject<Int> = BehaviorSubject.create()
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val bs_current_tab: BehaviorSubject<TypeTabCafe> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_cafe_id.subscribe(
            {
                base_networker.loadCafeSingle(it,
                    { cafe ->
                        ps_to_toggle_overlay.onNext(Pair(false, 500))
                        bs_cafe.onNext(cafe)
                    })
            })
                .disposeBy(composite_disposable)

        bs_cafe.subscribe(
            {

                if (it.hasHotDrinks())
                {
                    bs_current_tab.onNext(TypeTabCafe.HOT)
                }
                else if (it.hasColdDrinks())
                {
                    bs_current_tab.onNext(TypeTabCafe.COLD)
                }
                else if (it.hasSnacks())
                {
                    bs_current_tab.onNext(TypeTabCafe.SNACKS)
                }
                else
                {
                    throw RuntimeException("**** Error no items in menu ****")
                }

                BasketManager.bs_cafe.onNext(it)
            })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : ActCafeMenuListener
    {
        override fun clickedTab(type: TypeTabCafe)
        {
            bs_current_tab.onNext(type)
        }
    }
}