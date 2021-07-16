package com.justordercompany.client.logic.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.extensions.toJsonMy
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.getSumPrice
import com.justordercompany.client.logic.models.toServerBasketItems
import ru.yandex.money.android.sdk.*
import java.util.*
import kotlin.collections.ArrayList

class PaymentManager
{
    companion object
    {
        val color_scheme = ColorScheme(getColorMy(R.color.orange))

        fun makePay(items: ArrayList<ModelBasketItem>, methods: Set<PaymentMethodType>, success_action: (TokenizationResult) -> Unit, fail_action: (() -> Unit)? = null)
        {
            val activity = AppClass.top_activity ?: return
            //Todo make later if needed other currencies
            val currency = Currency.getInstance("RUB")
            val amount = Amount(items.getSumPrice().toBigDecimal(), currency)

            Log.e("PaymentManager", "makePay: Amount is $amount")

            val title = getStringMy(R.string.payment)
            val message = getStringMy(R.string.order_in_app_joc)
            val pay_params = PaymentParameters(amount, title, message, Constants.Payments.API_KEY, Constants.Payments.SHOP_ID, SavePaymentMethod.OFF, methods)
            val ui_params = UiParameters(false, color_scheme)

            ////
            val test_params = TestParameters(true, true)
            ///
            val intent = Checkout.createTokenizeIntent(activity, pay_params, uiParameters = ui_params)
            activity.startForResult(intent)
            { result ->

                val token_result = Checkout.createTokenizationResult(result.data!!)
                success_action(token_result)
            }.onFailed(
                { result ->

                    fail_action?.invoke()
                })
        }

        fun make3dSecureIntent(url: String, success_action: () -> Unit, fail_action: ((data: Intent?) -> Unit)? = null)
        {
            val activity = AppClass.top_activity ?: return

            val intent = Checkout.create3dsIntent(activity, url, color_scheme)
            activity.startForResult(intent)
            { result ->

                success_action()

            }.onFailed(
                { result ->

                    if (result.requestCode == Checkout.RESULT_ERROR)
                    {
                        fail_action?.invoke(result.data)
                    }
                })
        }

        fun makePayWithMethod(base_vm: BaseViewModel, methods: Set<PaymentMethodType>, order_id: Int, action_success: (Int) -> Unit)
        {
            val items = BasketManager.bs_items.value ?: return

            PaymentManager.makePay(items, methods,
                {
                    val token = it.paymentToken

                    base_vm.base_networker.payOrder(token, order_id,
                        {
                            val confirmation_url = it.getString("confirmation_url")

                            if (!confirmation_url.isNullOrEmpty())
                            {
                                PaymentManager.make3dSecureIntent(confirmation_url,
                                    {
                                        action_success(order_id)
                                    })
                            }
                            else
                            {
                                action_success(order_id)
                            }
                        })
                })
        }

        fun createOrder(base_vm: BaseViewModel, date: Date, comment: String?, cafe_id: Int, action_success: (Int) -> Unit)
        {
            val items = BasketManager.bs_items.value?.toServerBasketItems()

            if (items == null || items.size == 0)
            {
                val text = getStringMy(R.string.basket_is_empty)
                base_vm.showRedAlerter(text)
                return
            }

            val items_str = items.toJsonMy() ?: return
            val str_date = date.formatToString(DateManager.FORMAT_FOR_SERVER)
            val methods = setOf(PaymentMethodType.BANK_CARD, PaymentMethodType.GOOGLE_PAY)

            if (BasketManager.order_id != null)
            {
                makePayWithMethod(base_vm, methods, BasketManager.order_id!!, action_success)
            }
            else
            {
                base_vm.base_networker.makeOrder(str_date, comment, items_str,cafe_id,
                    { order_id ->

                        BasketManager.order_id = order_id
                        makePayWithMethod(base_vm, methods, order_id, action_success)
                    })
            }
        }
    }

}