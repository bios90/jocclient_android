package com.justordercompany.client.ui.screens.act_cafe_popup

import android.content.Intent
import android.location.Location
import android.text.format.DateUtils
import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.responses.RespBaseWithData
import com.justordercompany.client.logic.responses.RespCafeSingle
import com.justordercompany.client.logic.utils.*
import com.justordercompany.client.networking.apis.ApiCafe
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmCafePopup : BaseViewModel()
{
    val bs_cafe_id: BehaviorSubject<Int> = BehaviorSubject.create()
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val ps_cafe_distance_text: PublishSubject<String> = PublishSubject.create()
    val bs_route_info: BehaviorSubject<RouteInfoMy> = BehaviorSubject.create()
    val ps_route_time_text: PublishSubject<Optional<String>> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_cafe_id.subscribe(
            {
                base_networker.loadCafeSingle(it,
                    { cafe ->
                        ps_to_toggle_overlay.onNext(Pair(false, 500))
                        bs_cafe.onNext(cafe)
                        val cafe_lat_lng = cafe.getLatLng() ?: return@loadCafeSingle

                        location_manager.getLocationSingle()
                                .subscribe(
                                    {

                                        val user_pos = it.toLatLng()

                                        val route_info = RoutesManager.getRouteInfo(user_pos, cafe_lat_lng)
                                        if (route_info != null)
                                        {
                                            bs_route_info.onNext(route_info)
                                        }
                                    },
                                    {
                                        it.printStackTrace()
                                    })
                                .disposeBy(composite_disposable)
                    })
            })
                .disposeBy(composite_disposable)

        bs_route_info
                .subscribe(
                    {
                        val text_distance = LocationManager.getDistanceText(it.distance_in_meters.toInt())
                        ps_cafe_distance_text.onNext(text_distance)

                        val text_time = getTextRouteTime(it.time_in_seconds)
                        ps_route_time_text.onNext(text_time.asOptional())
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener() : ActCafePopupListener
    {
        override fun clickedVisit()
        {
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_CLICKED_VISIT, true)
            ps_to_finish.onNext(return_intent.asOptional())
        }

        override fun clickedRoute()
        {
            val route_into = bs_route_info.value ?: return
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_ROUTE_INFO, route_into)
            ps_to_finish.onNext(return_intent.asOptional())
        }
    }
}