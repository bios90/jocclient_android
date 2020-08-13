package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.logic.models.BaseImage
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.toLatLng
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class VmTabCafePage : BaseViewModel()
{
    val bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    val ps_cafe_distance_text: PublishSubject<String> = PublishSubject.create()

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
                                val distance = LocationManager.getDistanceText(cafe_lat_lng, user_pos)
                                ps_cafe_distance_text.onNext(distance)
                            },
                            {
                                it.printStackTrace()
                            })
                        .disposeBy(composite_disposable)
            })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener:TabCafePageListener
    {
        override fun clickedImage(image: BaseImage)
        {
            val all_images = bs_cafe.value?.getAllImagesWithLogo() ?: return
            val pos = all_images.indexOf(image)

            ps_to_show_images_slider.onNext(Pair(all_images,pos))
        }

        override fun clickedLogo()
        {
            val all_images = bs_cafe.value?.getAllImagesWithLogo() ?: return
            ps_to_show_images_slider.onNext(Pair(all_images,null))
        }
    }

}