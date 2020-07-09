package com.justordercompany.client.ui.screens.act_cafe_popup

import android.location.Location
import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelMapPos
import com.justordercompany.client.logic.responses.RespBaseWithData
import com.justordercompany.client.logic.responses.RespCafeSingle
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.networking.apis.ApiCafe
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmCafePopup : BaseViewModel()
{
    @Inject
    lateinit var api_cafe: ApiCafe

    val bs_cafe_id: BehaviorSubject<Int> = BehaviorSubject.create()
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val ps_cafe_distance_text: PublishSubject<String> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    override fun viewAttached()
    {
        super.viewAttached()
        addBottomSlider()
    }

    fun setEvents()
    {
        bs_cafe_id.subscribe(
            {
                Networking().loadCafeSingle(it,
                    { cafe ->
                        ps_to_toggle_overlay.onNext(Pair(false, 500))
                        bs_cafe.onNext(cafe)
                        val cafe_lat_lng = cafe.getLatLng() ?: return@loadCafeSingle

                        cafe.menu?.forEach({
                            it.products?.forEach(
                                {
                                    Log.e("VmCafePopup", "setEvents: categ is ${it.price}")
                                })

                        })

                        location_manager.getLocationSingle()
                                .subscribe(
                                    {
                                        val user_pos = it.toLatLng()
                                        val distance = LocationManager.getDistanceText(cafe_lat_lng, user_pos)
                                        ps_cafe_distance_text.onNext(distance)
                                    },
                                    {
                                        it.printStackTrace()
                                    })
                                .disposeBy(composite_disposable)
                    })
            })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener() : ActCafePopupListener
    {

    }

    inner class Networking()
    {
        fun loadCafeSingle(id: Int, action_success: (ModelCafe) -> Unit)
        {
            api_cafe.getCafeSingle(id)
                    .mainThreaded()
                    .addMyParser<RespCafeSingle>(RespCafeSingle::class.java)
                    .addProgress(this@VmCafePopup)
                    .addScreenDisabling(this@VmCafePopup)
                    .addErrorCatcher(this@VmCafePopup)
                    .addParseChecker({ it.cafe != null })
                    .subscribeMy(
                        {
                            action_success(it.cafe!!)
                        })
                    .disposeBy(composite_disposable)
        }
    }
}