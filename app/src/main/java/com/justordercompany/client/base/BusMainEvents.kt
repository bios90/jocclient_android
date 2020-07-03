package com.justordercompany.client.base

import android.util.Log
import com.justordercompany.client.base.enums.TypeTab
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BusMainEvents
{
    val bs_current_tab: BehaviorSubject<TypeTab> = BehaviorSubject.createDefault(TypeTab.LIST)
    val ps_user_logged:PublishSubject<Any> = PublishSubject.create()

    init
    {

    }
}