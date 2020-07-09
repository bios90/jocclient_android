package com.justordercompany.client.ui.screens.act_filter

import android.content.Intent
import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeCafe
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.myPutExtra
import com.justordercompany.client.logic.models.FilterData
import io.reactivex.subjects.BehaviorSubject

class VmActFilter : BaseViewModel()
{
    val bs_min_max: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.createDefault(Pair(0, 1000))
    val bs_cafe_type: BehaviorSubject<TypeCafe> = BehaviorSubject.createDefault(TypeCafe.ALL)
    val bs_rating: BehaviorSubject<Float> = BehaviorSubject.createDefault(4.0f)
    val bs_filter_from_extra: BehaviorSubject<Optional<FilterData>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_filter_from_extra
                .subscribe(
                    {
                        val filter_data = it.value ?: return@subscribe

                        Log.e("VmActFilter", "setEvents: got filter with rating ${filter_data.rating}")

                        bs_min_max.onNext(Pair(filter_data.price_min,filter_data.price_max))
                        bs_cafe_type.onNext(filter_data.type_cafe)
                        bs_rating.onNext(filter_data.rating)
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : ActFilterListener
    {
        override fun clickedOk()
        {
            val price_min = bs_min_max.value?.first ?: return
            val price_max = bs_min_max.value?.second ?: return
            val type_cafe = bs_cafe_type.value ?: return
            val rating = bs_rating.value ?: return

            val filter = FilterData(price_min, price_max, type_cafe, rating)
            val return_intent = Intent()
            return_intent.myPutExtra(Constants.Extras.EXTRA_FILTER, filter)
            ps_to_finish.onNext(return_intent.asOptional())
        }

        override fun clickedCancel()
        {

        }
    }
}