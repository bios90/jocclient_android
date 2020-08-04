package com.justordercompany.client.ui.screens.act_product_setting

import android.content.Intent
import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.logic.models.ModelAddableValue
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelProduct
import io.reactivex.subjects.BehaviorSubject

class VmActProductSetting : BaseViewModel()
{
    val bs_product: BehaviorSubject<ModelProduct> = BehaviorSubject.create()
    val bs_busket_item: BehaviorSubject<ModelBasketItem> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_product
                .subscribe(
                    {

                        runActionWithDelay(50,
                            {
                                if (bs_busket_item.value == null)
                                {
                                    val basket_item = it.toDefaultBasketItem()
                                    bs_busket_item.onNext(basket_item)
                                }
                            })
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : ActProductSettingListener
    {
        override fun sugarChangedTo(pos: Int)
        {
            val item = bs_busket_item.value ?: return
            if (item.sugar == pos)
            {
                return
            }
            item.sugar = pos
            bs_busket_item.onNext(item)
        }

        override fun weightChangedTo(weight: ModelAddableValue)
        {
            val item = bs_busket_item.value ?: return
            if (item.weight == weight)
            {
                return
            }
            item.weight = weight
            bs_busket_item.onNext(item)
        }

        override fun milkChangedTo(milk: ModelAddableValue)
        {
            val item = bs_busket_item.value ?: return
            if (item.milk == milk)
            {
                return
            }
            item.milk = milk
            bs_busket_item.onNext(item)
        }

        override fun addablesChangedTo(addables: List<ModelAddableValue>)
        {
            val item = bs_busket_item.value ?: return
            if (item.addables?.equals(addables) == true)
            {
                return
            }
            item.addables = addables
            bs_busket_item.onNext(item)
        }

        override fun clickedAdd()
        {
            val item = bs_busket_item.value ?: return
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_BASKET_ITEM, item)
            ps_to_finish.onNext(return_intent.asOptional())
        }
    }
}