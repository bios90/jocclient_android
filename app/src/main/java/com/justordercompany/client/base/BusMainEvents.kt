package com.justordercompany.client.base

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.logic.models.FilterData
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BusMainEvents
{
    val bs_current_tab: BehaviorSubject<TypeTab> = BehaviorSubject.createDefault(TypeTab.LIST)
    val bs_filter:BehaviorSubject<FilterData> = BehaviorSubject.createDefault(FilterData())
    val bs_current_user_position:BehaviorSubject<LatLng> = BehaviorSubject.create()

    //User
    val ps_user_logged:PublishSubject<Any> = PublishSubject.create()
    val ps_user_profile_updated:PublishSubject<Any> = PublishSubject.create()

    init
    {

    }
}