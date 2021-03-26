package com.justordercompany.client.ui.screens.act_cafe_menu

import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeTabCafe
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.models.*
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import io.reactivex.subjects.BehaviorSubject
import java.lang.RuntimeException

class VmActCafeMenu : BaseViewModel()
{
    val bs_cafe_id: BehaviorSubject<Int> = BehaviorSubject.create()
    val bs_order_id: BehaviorSubject<Int> = BehaviorSubject.create()
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val bs_order: BehaviorSubject<ModelOrder> = BehaviorSubject.create()
    val bs_current_tab: BehaviorSubject<TypeTabCafe> = BehaviorSubject.createDefault(TypeTabCafe.CAFE)

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_cafe_id.subscribe(
            { cafe_id ->

                val current_basket_cafe_id = BasketManager.bs_cafe.value?.value?.id
                val items_count = BasketManager.bs_items.value?.size ?: 0

                if (current_basket_cafe_id != null
                        && current_basket_cafe_id != cafe_id
                        && items_count > 0)
                {
                    val title = getStringMy(R.string.basket)
                    val message = getStringMy(R.string.basket_already_contains)

                    val builder = BuilderDialogMy()
                            .setViewId(R.layout.la_dialog_simple)
                            .setTitle(title)
                            .setText(message)
                            .setBtnOk(BtnAction(getStringMy(R.string.clear),
                                {
                                    BasketManager.clearBasket()
                                    bs_cafe_id.onNext(cafe_id)
                                }))
                            .setBtnCancel(BtnAction(getStringMy(R.string._continue),
                                {
                                    bs_cafe_id.onNext(current_basket_cafe_id)
                                }))
                    ps_to_show_dialog.onNext(builder)
                }
                else
                {
                    base_networker.loadCafeSingle(cafe_id,
                        { cafe ->
                            ps_to_toggle_overlay.onNext(Pair(false, 500))
                            bs_cafe.onNext(cafe)

                            bs_order_id.value?.let(
                                {
                                    base_networker.getOrderInfo(it,
                                        {
                                            bs_order.onNext(it)
                                        })
                                })
                        })
                }


            })
                .disposeBy(composite_disposable)

        bs_cafe.subscribe(
            {

                val current_cafe_id = BasketManager.bs_cafe.value?.value?.id
                if (current_cafe_id != null && current_cafe_id != it.id)
                {
                    BasketManager.clearBasket()
                }

                bs_current_tab.onNext(TypeTabCafe.CAFE)

                BasketManager.bs_cafe.onNext(it.asOptional())
            })
                .disposeBy(composite_disposable)

        bs_order.subscribe(
            {
                BasketManager.setOrder(it)
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