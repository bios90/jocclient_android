package com.justordercompany.client.logic.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.getSumPrice
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

            val title = getStringMy(R.string.payment)
            val message = getStringMy(R.string.order_in_app_joc)
            val pay_params = PaymentParameters(amount, title, message, Constants.Payments.API_KEY, Constants.Payments.SHOP_ID, SavePaymentMethod.USER_SELECTS, methods)
            val ui_params = UiParameters(false, color_scheme)
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
    }

}