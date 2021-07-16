package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import android.content.Intent
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.BusMainEvents
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.BaseImage
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelReview
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.RouteInfoMy
import com.justordercompany.client.logic.utils.RoutesManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.logic.utils.toLatLng
import com.justordercompany.client.ui.screens.act_review_dialog.ActReviewDialog
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

                                Thread(
                                    {
                                        val route_info = RoutesManager.getRouteInfo(user_pos, cafe_lat_lng)

                                        if (route_info != null)
                                        {
                                            runActionOnMainThread(
                                                {
                                                    bs_route_info.onNext(route_info)
                                                })

                                        }
                                    })
                                        .start()
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

    private fun reloadCafe()
    {
        val cafe_id = bs_cafe.value?.id ?: return
        base_networker.loadCafeSingle(cafe_id,
            { cafe ->
                bs_cafe.onNext(cafe)
            })
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
            bus_main_events.ps_clicked_cafe_route.onNext(route_into)
            ps_to_finish.onNext(Optional(null))
        }

        override fun clickedAddReview()
        {
            val cafe_id = bs_cafe.value?.id ?: return
            val builder = BuilderIntent()
                    .setActivityToStart(ActReviewDialog::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
                    .setOkAction(
                        {
                            if (it?.getBooleanExtra(Constants.Extras.EXTRA_REVIEW_MADE, false) == true)
                            {
                                reloadCafe()
                            }
                        })
            ps_intent_builded.onNext(builder)
        }

        override fun clickedReview(review: ModelReview)
        {
//            val order_id = order.id ?: return
//            val builder = BuilderIntent()
//                    .setActivityToStart(ActReviewDialog::class.java)
//                    .addParam(Constants.Extras.EXTRA_ORDER_ID, order_id)
//                    .setOkAction(
//                        {
//                            if (it?.getBooleanExtra(Constants.Extras.EXTRA_REVIEW_MADE, false) == true)
//                            {
//                                reloadUserInfo()
//                            }
//                        })
//            ps_intent_builded.onNext(builder)
        }

        override fun clickedToggleFavorite()
        {
            val cafe = bs_cafe.value ?: return
            val cafe_id = cafe.id ?: return

            val favorite = !cafe.isFavorite()
            base_networker.toggleCafeFavorite(cafe_id, favorite,
                {
                    reloadCafe()
                    val text = if(favorite) getStringMy(R.string.cafe_added_to_favorites) else getStringMy(R.string.cafe_removed_from_favorites)
                    BuilderAlerter.getGreenBuilder(text).sendInVm(this@VmTabCafePage)
                })
        }
    }

}