package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_basket

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_order_dialog.ActOrderDialog
import com.justordercompany.client.ui.screens.act_product_setting.ActProductSetting
import io.reactivex.subjects.BehaviorSubject

class VmTabBasket : BaseViewModel()
{
    val bs_basket_items: BehaviorSubject<ArrayList<ModelBasketItem>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        BasketManager.bs_items.subscribe(
            {
                bs_basket_items.onNext(it)
            })
                .disposeBy(composite_disposable)

    }

    inner class ViewListener : TabBasketListener
    {
        override fun clickedEdit(item: ModelBasketItem)
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActProductSetting::class.java)
                    .addParam(Constants.Extras.EXTRA_BASKET_ITEM, item)
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
        }
    }
}