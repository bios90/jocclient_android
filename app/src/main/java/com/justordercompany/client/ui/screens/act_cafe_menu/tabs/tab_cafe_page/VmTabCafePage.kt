package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import android.content.Intent
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.BaseImage
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.RouteInfoMy
import com.justordercompany.client.logic.utils.RoutesManager
import com.justordercompany.client.logic.utils.toLatLng
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class VmTabCafePage : BaseViewModel()
{
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val ps_cafe_distance_text: PublishSubject<String> = PublishSubject.create()
    val bs_route_info: BehaviorSubject<RouteInfoMy> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    fun setEvents()
    {
        bs_cafe.subscribe(
            {
                val cafe_lat_lng = it.getLatLng() ?: return@subscribe

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
                .disposeBy(composite_disposable)

        bs_route_info
                .subscribe(
                    {
                        val text_distance = LocationManager.getDistanceText(it.distance_in_meters.toInt())
                        ps_cafe_distance_text.onNext(text_distance)
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : TabCafePageListener
    {
        override fun clickedImage(image: BaseImage)
        {
            val all_images = bs_cafe.value?.getAllImagesWithLogo() ?: return
            val pos = all_images.indexOf(image)

            ps_to_show_images_slider.onNext(Pair(all_images, pos))
        }

        override fun clickedLogo()
        {
            val all_images = bs_cafe.value?.getAllImagesWithLogo() ?: return
            ps_to_show_images_slider.onNext(Pair(all_images, null))
        }

        override fun clickedDistance()
        {
            val route_into = bs_route_info.value ?: return
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_ROUTE_INFO, route_into)
            ps_to_finish.onNext(return_intent.asOptional())
        }
    }

}