package com.justordercompany.client.base

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.logic.models.FilterData
import com.justordercompany.client.logic.utils.RouteInfoMy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BusMainEvents
{
    val bs_current_tab: BehaviorSubject<TypeTab> = BehaviorSubject.createDefault(TypeTab.MAP)
    val bs_filter: BehaviorSubject<FilterData> = BehaviorSubject.createDefault(FilterData())
    val bs_current_map_center:BehaviorSubject<LatLng> = BehaviorSubject.create()

    //User
    val ps_user_logged: PublishSubject<Any> = PublishSubject.create()
    val ps_user_profile_updated: PublishSubject<Any> = PublishSubject.create()

    //Orders
    val bs_order_made: BehaviorSubject<Int> = BehaviorSubject.create()
    val ps_clicked_cafe_route: PublishSubject<RouteInfoMy> = PublishSubject.create()

}