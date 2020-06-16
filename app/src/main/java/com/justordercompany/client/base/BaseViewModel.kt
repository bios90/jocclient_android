package com.justordercompany.client.base

import androidx.lifecycle.ViewModel
import com.justordercompany.client.logic.utils.BuilderIntent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

abstract class BaseViewModel:ViewModel()
{
    @Inject
    lateinit var bus_main_events:BusMainEvents

    val composite_disposable = CompositeDisposable()
    val ps_intent_builded: PublishSubject<BuilderIntent> = PublishSubject.create()
    val ps_show_hide_progress: PublishSubject<Boolean> = PublishSubject.create()

    override fun onCleared()
    {
        composite_disposable.dispose()
        super.onCleared()
    }
}