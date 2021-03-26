package com.justordercompany.client.ui.screens.act_main.tabs.list

import com.justordercompany.client.base.*
import com.justordercompany.client.base.enums.TypeLaListIntroMode
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.openAppSettings
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.countDistanceFrom
import com.justordercompany.client.logic.requests.ReqCafes
import com.justordercompany.client.logic.utils.BuilderPermRequest
import com.justordercompany.client.logic.utils.PermissionManager
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
    val bs_intro_mode: BehaviorSubject<TypeLaListIntroMode> = BehaviorSubject.createDefault(TypeLaListIntroMode.SEARCHING)

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
        checkIfLocationBlocked()

        runActionWithDelay(2000,
            {
                reloadCafes()
            })
                .disposeBy(composite_disposable)
    }

    fun setEvents()
    {
        location_manager
                .bs_location
                .subscribe(
                    {
                        reloadCafes()
                        bs_intro_mode.onNext(TypeLaListIntroMode.FOUND)
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
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(
                    { req ->
                        base_networker.loadCafes(req,
                            {
                                req.action_success?.invoke(it)
                            })
                    })
                .disposeBy(composite_disposable)
    }

    fun checkIfLocationBlocked()
    {
        val builder = BuilderPermRequest()
                .setPermissions(PermissionManager.permissions_location)
                .setActionBlockedFinally(
                    {
                        bs_intro_mode.onNext(TypeLaListIntroMode.DISABLED)
                    })
        ps_request_permissions.onNext(builder)
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

    inner class ViewListener : TabListListener
    {
        override fun clickedCafe(cafe: ModelCafe)
        {
            val cafe_id = cafe.id ?: return

            val builder = BuilderIntent()
                    .setActivityToStart(ActCafeMenu::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
            ps_intent_builded.onNext(builder)
        }

        override fun clickedToSettings()
        {
            openAppSettings()
        }
    }

}