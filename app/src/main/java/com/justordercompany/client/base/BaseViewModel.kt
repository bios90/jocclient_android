package com.justordercompany.client.base

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.logic.utils.BuilderPermRequest
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

abstract class BaseViewModel:ViewModel()
{
    @Inject
    lateinit var bus_main_events:BusMainEvents

    @Inject
    lateinit var location_manager:LocationManager

    val composite_disposable = CompositeDisposable()

    val ps_intent_builded: PublishSubject<BuilderIntent> = PublishSubject.create()
    val ps_show_hide_progress: PublishSubject<Boolean> = PublishSubject.create()
    val ps_enable_disable_screen_touches: PublishSubject<Boolean> = PublishSubject.create()
    val ps_to_show_dialog: PublishSubject<BuilderDialogMy> = PublishSubject.create()
    val ps_to_show_alerter:PublishSubject<BuilderAlerter> = PublishSubject.create()
    val ps_request_permissions: PublishSubject<BuilderPermRequest> = PublishSubject.create()
    val ps_to_finish:PublishSubject<Optional<Intent>> = PublishSubject.create()

    override fun onCleared()
    {
        composite_disposable.dispose()
        super.onCleared()
    }

    fun clickedBack()
    {
        ps_to_finish.onNext(Optional(null))
    }

    abstract fun activityAttached()
}