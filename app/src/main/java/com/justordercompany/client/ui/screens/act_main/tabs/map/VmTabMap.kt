package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.PmCafeSort
import com.justordercompany.client.base.enums.PmSortDirection
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.responses.RespCafes
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.networking.apis.ApiCafe
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmTabMap : BaseViewModel()
{
    @Inject
    lateinit var api_cafes:ApiCafe

    var ps_move_map_pos: PublishSubject<ModelMapPos> = PublishSubject.create()
    var bs_current_visible_distance:BehaviorSubject<Float> = BehaviorSubject.create()
    var bs_current_center:BehaviorSubject<LatLng> = BehaviorSubject.create()

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
    }

    private fun moveMapToUserLocation()
    {
        location_manager.getLocationSingle()
                .subscribe(
                    {
                        val map_pos = ModelMapPos(it.toLatLng())
                        ps_move_map_pos.onNext(map_pos)
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

        override fun mapIdled()
        {
            val distance = bs_current_visible_distance.value ?: return
            val center = bs_current_center.value ?: return


            api_cafes.getCafes(center.latitude,center.longitude,distance.toInt(),PmCafeSort.DISTANCE,PmSortDirection.ASC)
                    .mainThreaded()
                    .addMyParser<RespCafes>(RespCafes::class.java)
                    .addProgress(this@VmTabMap)
                    .addScreenDisabling(this@VmTabMap)
                    .addErrorCatcher(this@VmTabMap)
                    .addParseChecker({ it.cafes != null })
                    .subscribeMy(
                        {
                            Log.e("ViewListener", "mapIdled: Loaded cafes count is ${it.cafes?.size}")
                        })
                    .disposeBy(composite_disposable)
        }
    }
}