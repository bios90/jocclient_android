package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.PaymentManager
import com.justordercompany.client.logic.utils.addMinutes
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_auth.ActAuth
import com.justordercompany.client.ui.screens.act_order_dialog.ActOrderDialog
import com.justordercompany.client.ui.screens.act_product_setting.ActProductSetting
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import kotlin.collections.ArrayList

class VmTabBasket : BaseViewModel()
{
    var bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val bs_basket_items: BehaviorSubject<ArrayList<ModelBasketItem>> = BehaviorSubject.create()
    val bs_show_register: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
        checkLogin()
    }

    fun checkLogin()
    {
        bs_show_register.onNext(SharedPrefsManager.getCurrentUser() == null)
    }

    fun setEvents()
    {
        BasketManager.bs_items.subscribe(
            {
                bs_basket_items.onNext(it)
            })
                .disposeBy(composite_disposable)

        bus_main_events.ps_user_logged
                .subscribe(
                    {
                        checkLogin()
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_order_made
                .subscribe(
                    {
                        ps_to_finish.onNext(Optional(null))
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : TabBasketListener
    {
        override fun clickedEdit(item: ModelBasketItem)
        {
            val can_order = bs_cafe.value?.can_order == true
            val builder = BuilderIntent()
                    .setActivityToStart(ActProductSetting::class.java)
                    .addParam(Constants.Extras.EXTRA_BASKET_ITEM, item)
                    .addParam(Constants.Extras.EXTRA_CAN_ORDER, can_order)
                    .setOkAction(
                        {
                            val item = it?.getSerializableExtra(Constants.Extras.EXTRA_BASKET_ITEM) as? ModelBasketItem
                            if (item != null)
                            {
                                BasketManager.updateItem(item)
                            }
                        })
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)

            ps_intent_builded.onNext(builder)
        }

        override fun clickedDelete(item: ModelBasketItem)
        {
            BasketManager.removeItem(item)
        }

        override fun clickedOrder()
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActOrderDialog::class.java)
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)
            ps_intent_builded.onNext(builder)
        }

        override fun clickedQuickOrder()
        {
            val date = Date().addMinutes(5)
            val cafe_id = bs_cafe.value?.id ?: return
            PaymentManager.createOrder(this@VmTabBasket, date, null, cafe_id,
                {
                    bus_main_events.bs_order_made.onNext(it)
                })
        }

        override fun clickedRegister()
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActAuth::class.java)
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)
            ps_intent_builded.onNext(builder)
        }
    }
}