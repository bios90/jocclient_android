package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.networking.apis.ApiAuth
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmMainMap : BaseViewModel()
{
    var ps_move_map_pos: PublishSubject<ModelMapPos> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)
    }

    override fun activityAttached()
    {

    }

    private fun moveMapToUserLocation()
    {
        location_manager.getLocationUpdates()
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

    inner class ViewListener() : LaMainMapListener
    {
        override fun mapInited()
        {
            moveMapToUserLocation()
        }
    }
}