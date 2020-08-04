package com.justordercompany.client.ui.screens.act_order_dialog

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.*
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.models.toServerBasketItems
import com.justordercompany.client.logic.utils.*
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import ru.yandex.money.android.sdk.PaymentMethodType
import java.util.*

class VmActOrderDialog : BaseViewModel()
{
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val bs_time: BehaviorSubject<Date> = BehaviorSubject.create()
    val bs_comment: BehaviorSubject<Optional<String>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
        bs_time.onNext(Date().addMinutes(15))
    }

    private fun setEvents()
    {
        BasketManager.bs_cafe
                .subscribe(
                    {
                        bs_cafe.onNext(it)
                    })
                .disposeBy(composite_disposable)
    }

    private fun makePayWithMethod(method: PaymentMethodType, order_id: Int)
    {
        val items = BasketManager.bs_items.value ?: return

        PaymentManager.makePay(items, setOf(method),
            {
                val token = it.paymentToken

                base_networker.payOrder(token, order_id,
                    {
                        val confirmation_url = it.getString("confirmation_url")

                        if (confirmation_url != null)
                        {
                            PaymentManager.make3dSecureIntent(confirmation_url,
                                {
                                    handleAfterOrder(order_id)
                                })
                        }
                        else
                        {
                            handleAfterOrder(order_id)
                        }
                    })
            })
    }

    private fun handleAfterOrder(order_id: Int)
    {
        base_networker.getOrderInfo(order_id,
            {
                //Todo make logic with pass check!!!
                bus_main_events.bs_order_made.onNext(order_id)
                ps_to_finish.onNext(Optional(null))

            })

//        var disposable: Disposable? = null
//        val action: (Int) -> Unit =
//                { count ->
//
//                    base_networker.getOrderInfo(order_id,
//                        {
//                            Log.e("VmActOrderDialog", "handleAfterOrder: Got orders, its status is ${it.status}")
//                        })
//                }
//
//        disposable = runRepeatingAction(5000, action)
    }


    inner class ViewListener : ActOrderDialogListener
    {
        override fun clickedOrder()
        {
            val items = BasketManager.bs_items.value?.toServerBasketItems()

            if (items == null || items.size == 0)
            {
                val text = getStringMy(R.string.basket_is_empty)
                showRedAlerter(text)
                return
            }

            val items_str = items.toJsonMy() ?: return
            val str_date = bs_time.value?.formatToString(DateManager.FORMAT_FOR_SERVER) ?: return
            val comment = bs_comment.value?.value

            base_networker.makeOrder(str_date, comment, items_str,
                { order_id ->

                    makePayWithMethod(PaymentMethodType.BANK_CARD, order_id)

                    //Old variant with dialog
//                    BuilderIntent()
//                            .setActivityToStart(ActPayTypeDialog::class.java)
//                            .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)
//                            .setOkAction(
//                                {
//
//                                    if (it?.checkIntentExtraBool(Constants.Extras.EXTRA_PAY_WITH_CARD) == true)
//                                    {
//                                        runActionWithDelay(100,
//                                            {
//                                                makePayWithMethod(PaymentMethodType.BANK_CARD, order_id)
//                                            })
//                                    }
//                                    else if (it?.checkIntentExtraBool(Constants.Extras.EXTRA_PAY_WITH_GOOGLE_PAY) == true)
//                                    {
//                                        runActionWithDelay(100,
//                                            {
//                                                makePayWithMethod(PaymentMethodType.GOOGLE_PAY, order_id)
//                                            })
//                                    }
//                                    //Todo mmake other for saved card
//                                    else
//                                    {
//
//                                    }
//                                })
//                            .sendInVm(this@VmActOrderDialog)
                })
        }
    }
}