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
                        val cafe = it.value ?: return@subscribe
                        bs_cafe.onNext(cafe)
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_order_made
                .subscribe(
                    {
                        ps_to_finish.onNext(Optional(null))
                    })
                .disposeBy(composite_disposable)
    }


    inner class ViewListener : ActOrderDialogListener
    {
        override fun clickedOrder()
        {
            val date = bs_time.value ?: return
            val comment = bs_comment.value?.value
            PaymentManager.createOrder(this@VmActOrderDialog, date, comment,
                {
                    bus_main_events.bs_order_made.onNext(it)
                })
        }
    }
}