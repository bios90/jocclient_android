package com.justordercompany.client.ui.screens.act_review_dialog

import android.content.Intent
import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getNullString
import com.justordercompany.client.logic.models.ModelOrder
import io.reactivex.subjects.BehaviorSubject

class VmActReviewDialog : BaseViewModel()
{
    var bs_order_id: BehaviorSubject<Int> = BehaviorSubject.create()
    var bs_order: BehaviorSubject<ModelOrder> = BehaviorSubject.create()

    var bs_review_text: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(Optional(String.getNullString()))
    var bs_rating: BehaviorSubject<Double> = BehaviorSubject.createDefault(5.0)

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    private fun setEvents()
    {
        bs_order_id
                .subscribe(
                    {
                        base_networker.getOrderInfo(it,
                            {
                                bs_order.onNext(it)
                            })
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener() : ActReviewDialogListener
    {
        override fun clickedOk()
        {
            val cafe_id = bs_order.value?.cafe?.id ?: return
            val order_id = bs_order_id.value ?: return
            val rating = bs_rating.value?.toInt() ?: return
            val text = bs_review_text.value?.value

            base_networker.makeOrderReview(cafe_id, order_id, text, rating,
                {
                    val return_intent = Intent()
                    return_intent.putExtra(Constants.Extras.EXTRA_REVIEW_MADE,true)
                    ps_to_finish.onNext(Optional(return_intent))
                })
        }

        override fun clickedCancel()
        {
            ps_to_finish.onNext(Optional(null))
        }
    }
}