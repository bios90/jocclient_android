package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.models.countDistanceFrom
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.networking.apis.ApiCafe
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_popup.ActCafePopup
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmTabMap : BaseViewModel()
{
    @Inject
    lateinit var api_cafes: ApiCafe

    var ps_move_map_pos: PublishSubject<ModelMapPos> = PublishSubject.create()
    var bs_current_visible_distance: BehaviorSubject<Float> = BehaviorSubject.create()
    var bs_current_center: BehaviorSubject<LatLng> = BehaviorSubject.create()
    var bs_cafe_to_display: BehaviorSubject<ArrayList<ModelCafe>> = BehaviorSubject.create()
    var bs_cafe_bottom_dialog: BehaviorSubject<ModelCafe> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    private fun setEvents()
    {
        bs_current_visible_distance.subscribe(
            {
                Log.e("VmTabMap", "setEvents: Current distenace is $it")
            })
                .disposeBy(composite_disposable)

        bus_main_events.bs_filter
                .subscribe(
                    {
                        val distance = bs_current_visible_distance.value ?: return@subscribe
                        val center = bs_current_center.value ?: return@subscribe

                        val request = ReqCafes()
                        request.lat = center.latitude
                        request.lon = center.longitude
                        request.distance = distance.toInt()
                        request.filter = it

                        base_networker.loadCafes(request,
                            {
                                bs_cafe_to_display.onNext(it)
                            })
                    })
                .disposeBy(composite_disposable)
    }

    private fun moveMapToUserLocation()
    {
        location_manager.getLocationSingle()
                .subscribe(
                    {
                        val map_pos = ModelMapPos(it.toLatLng())
                        ps_move_map_pos.onNext(map_pos)
                        bus_main_events.bs_current_user_position.onNext(it.toLatLng())
                    },
                    {
                        it.printStackTrace()
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener() : TabMapListener
    {
        override fun mapInited()
        {
//            moveMapToUserLocation()
        }

        override fun clickedCafe(cafe: ModelCafe)
        {
            val cafe_id = cafe.id ?: return

            val builder = BuilderIntent()
                    .setActivityToStart(ActCafePopup::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
                    .setOkAction(
                        {
                            if (it?.getBoolExtraMy(Constants.Extras.EXTRA_CLICKED_VISIT) == true)
                            {
                                val builder = BuilderIntent()
                                        .setActivityToStart(ActCafeMenu::class.java)
                                        .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)

                                ps_intent_builded.onNext(builder)
                            }
                        })
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)

            ps_intent_builded.onNext(builder)
        }

        override fun mapIdled()
        {
            val distance = bs_current_visible_distance.value ?: return
            val center = bs_current_center.value ?: return

            val request = ReqCafes()
            request.lat = center.latitude
            request.lon = center.longitude
            request.distance = distance.toInt()
            request.filter = bus_main_events.bs_filter.value

            base_networker.loadCafes(request,
                {

                    bs_cafe_to_display.onNext(it)
                })
        }
    }

}