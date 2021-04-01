package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeLaListIntroMode
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.models.countDistanceFrom
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.RouteInfoMy
import com.justordercompany.client.logic.utils.RoutesManager
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.networking.apis.ApiCafe
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_popup.ActCafePopup
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
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
    var ps_to_show_route: PublishSubject<PolylineOptions> = PublishSubject.create()

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

        location_manager
                .bs_location
                .subscribe(
                    {
                        val map_pos = ModelMapPos(it.toLatLng())
                        ps_move_map_pos.onNext(map_pos)

                        reloadCafes()
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_filter
                .subscribe(
                    {
                        reloadCafes()
                    })
                .disposeBy(composite_disposable)
    }

    private fun reloadCafes()
    {
        val distance = bs_current_visible_distance.value ?: 10f
        val center = bs_current_center.value ?: LatLng(55.751244, 37.618423)

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


    inner class ViewListener() : TabMapListener
    {
        override fun mapInited()
        {
        }

        override fun clickedCafe(cafe: ModelCafe)
        {
            val cafe_id = cafe.id ?: return

            val builder = BuilderIntent()
                    .setActivityToStart(ActCafePopup::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
                    .setOkAction(
                        {
                            val route_info = it?.getSerializableExtra(Constants.Extras.EXTRA_ROUTE_INFO) as? RouteInfoMy

                            if (route_info != null)
                            {
                                showRoute(route_info)
                            }
                            else if (it?.getBoolExtraMy(Constants.Extras.EXTRA_CLICKED_VISIT) == true)
                            {
                                toCafeShow(cafe_id)
                            }
                        })
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)

            ps_intent_builded.onNext(builder)
        }

        override fun mapIdled()
        {
            reloadCafes()
        }
    }

    private fun toCafeShow(cafe_id: Int)
    {
        val builder = BuilderIntent()
                .setActivityToStart(ActCafeMenu::class.java)
                .setOkAction(
                    {
                        val route_info = it?.getSerializableExtra(Constants.Extras.EXTRA_ROUTE_INFO) as? RouteInfoMy

                        if (route_info != null)
                        {
                            showRoute(route_info)
                        }
                    })
                .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)

        runActionWithDelay(300,
            {
                ps_intent_builded.onNext(builder)
            })
    }

    private fun showRoute(route_info: RouteInfoMy)
    {
        ps_to_show_route.onNext(route_info.getPolyInfo())
        location_manager.bs_location.value?.let(
            {
                ps_move_map_pos.onNext(ModelMapPos(it.toLatLng(), 15f))
            })
    }
}