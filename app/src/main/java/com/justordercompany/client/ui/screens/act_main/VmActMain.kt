package com.justordercompany.client.ui.screens.act_main

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.FilterData
import com.justordercompany.client.logic.utils.*
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogBottom
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_filter.ActFilter
import com.justordercompany.client.ui.screens.act_intro_slides.ActIntroSlides
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class VmActMain : BaseViewModel()
{
    val ps_scroll_to_tab: PublishSubject<TypeTab> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)

        setBusEvents()
    }

    override fun viewAttached()
    {
        //Made because some glithces on some devices
        runActionWithDelay(500,
            {
                checkGeoPermissions()
            })

        runActionWithDelay(1000,
            {
                if (!SharedPrefsManager.getBool(SharedPrefsManager.Key.MASK_INTRO_SHOWED))
                {
                    BuilderIntent()
                            .setActivityToStart(ActIntroSlides::class.java)
                            .sendInVm(this)
                    SharedPrefsManager.saveBool(SharedPrefsManager.Key.MASK_INTRO_SHOWED, true)
                }
            })
    }


    fun setBusEvents()
    {
        bus_main_events.bs_current_tab
                .mainThreaded()
                .subscribe(
                    {
                        ps_scroll_to_tab.onNext(it)
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_order_made
                .mainThreaded()
                .subscribe(
                    {
                        bus_main_events.bs_current_tab.onNext(TypeTab.PROFILE)
                    })
                .disposeBy(composite_disposable)
    }


    private fun checkGeoPermissions()
    {
        Log.e("VmActMain", "checkGeoPermissions: Called check geo perms!!!")
        val text_blocked = getStringMy(R.string.need_permissions_geo)

        val builder_perm_request = BuilderPermRequest()
                .setPermissions(PermissionManager.permissions_location)
                .setActionBlockedNow(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedNow(text_blocked,
                            {
                                checkGeoPermissions()
                            })
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionBlockedFinally(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedFinally(text_blocked)
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionAvailable(
                    {
                        location_manager.startGeoTracker()
                    })

        ps_request_permissions.onNext(builder_perm_request)
    }

    inner class ViewListener : ActMainListener
    {
        override fun clickedTab(tab: TypeTab)
        {
            bus_main_events.bs_current_tab.onNext(tab)
        }

        override fun clickedFilter()
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActFilter::class.java)
                    .addParam(Constants.Extras.EXTRA_FILTER, bus_main_events.bs_filter.value)
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)
                    .setOkAction(
                        {

                            val filter = it?.getSerializableExtra(Constants.Extras.EXTRA_FILTER) as? FilterData
                            filter ?: return@setOkAction

                            bus_main_events.bs_filter.onNext(filter)
                        })
            ps_intent_builded.onNext(builder)
        }
    }
}