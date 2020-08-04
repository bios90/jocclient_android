package com.justordercompany.client.ui.screens.act_main.tabs.list

import com.justordercompany.client.base.*
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.countDistanceFrom
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class VmTabList : BaseViewModel()
{
    var bs_current_cafes: BehaviorSubject<FeedDisplayInfo<ModelCafe>> = BehaviorSubject.create()
    val ps_to_load_cafes: PublishSubject<ReqCafes> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        location_manager
                .bs_location
                .subscribe(
                    {
                        reloadCafes()
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_filter
                .subscribe(
                    {
                        val location = location_manager.bs_location.value ?: return@subscribe

                        reloadCafes()
                    })
                .disposeBy(composite_disposable)

        ps_to_load_cafes
                .debounce(200,TimeUnit.MILLISECONDS)
                .subscribe(
                    { req ->
                        base_networker.loadCafes(req,
                            {
                                req.action_success?.invoke(it)
                            })
                    })
                .disposeBy(composite_disposable)
    }

    private fun reloadCafes()
    {
        val location = location_manager.bs_location.value ?: return
        val filter = bus_main_events.bs_filter.value ?: return

        val req = ReqCafes()
        req.lat = location.latitude
        req.lon = location.longitude
        req.distance = Constants.LIST_CAFE_LOAD_DISTACNE
        req.filter = filter
        req.action_success =
                {
                    it.countDistanceFrom(location.toLatLng())
                    val load_info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                    bs_current_cafes.onNext(load_info)
                }

        ps_to_load_cafes.onNext(req)
    }

    inner class ViewListener:TabListListener
    {
        override fun clickedCafe(cafe: ModelCafe)
        {
            val cafe_id = cafe.id ?: return

            val builder = BuilderIntent()
                    .setActivityToStart(ActCafeMenu::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
            ps_intent_builded.onNext(builder)
        }
    }

}