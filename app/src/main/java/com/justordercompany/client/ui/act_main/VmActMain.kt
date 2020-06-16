package com.justordercompany.client.ui.act_main

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.mainThreaded
import io.reactivex.subjects.PublishSubject

class VmActMain : BaseViewModel()
{
    val ps_scroll_to_tab: PublishSubject<TypeTab> = PublishSubject.create()

    init
    {
        AppClass.app_component.inject(this)

        setBusEvents()
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

    inner class ViewListener : ActMainListener
    {
        override fun clickedTab(tab: TypeTab)
        {
            bus_main_events.bs_current_tab.onNext(tab)
        }
    }
}