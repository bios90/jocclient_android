package com.justordercompany.client.ui.screens.act_main

import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.logic.utils.*
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogBottom
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
        checkGeoPermissions()
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
    }

    private fun checkGeoPermissions()
    {
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

        ps_request_permissions.onNext(builder_perm_request)
    }

    inner class ViewListener : ActMainListener
    {
        override fun clickedTab(tab: TypeTab)
        {
            bus_main_events.bs_current_tab.onNext(tab)
        }
    }
}