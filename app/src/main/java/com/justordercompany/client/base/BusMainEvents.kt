package com.justordercompany.client.base

import android.util.Log
import com.justordercompany.client.base.enums.TypeTab
import io.reactivex.subjects.BehaviorSubject

class BusMainEvents
{
    val bs_current_tab: BehaviorSubject<TypeTab> = BehaviorSubject.createDefault(TypeTab.LIST)

    init
    {

    }
}