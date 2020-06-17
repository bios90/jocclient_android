package com.justordercompany.client.ui.screens.act_auth

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.responses.BaseResponse
import com.justordercompany.client.networking.apis.ApiAuth
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import kotlin.math.log

class VmActAuth : BaseViewModel()
{
    @Inject
    lateinit var api_auth: ApiAuth

    var bs_mode:BehaviorSubject<ActAuth.Mode> = BehaviorSubject.createDefault(ActAuth.Mode.PHONE)

    init
    {
        AppClass.app_component.inject(this)
    }

    override fun activityAttached()
    {

    }

    inner class ViewListener : ActAuthListener
    {
        override fun clickedSend()
        {
                api_auth.makeRegister("bios90@mail.ri")
                        .mainThreaded()
                        .addMyParser<BaseResponse>(BaseResponse::class.java)
                        .addProgress(this@VmActAuth)
                        .addScreenDisabling(this@VmActAuth)
                        .addErrorCatcher(this@VmActAuth)
//                        .subscribe(
//                            {
//                                Log.e("ViewListener", "clickedSend: Successs")
//                            },
//                            {
//                                Log.e("ViewListener", "clickedSend: Error")
//                                it.printStackTrace()
//                            })
//                        .disposeBy(composite_disposable)

//            var mode = bs_mode.value!!
//            if(mode == ActAuth.Mode.PHONE)
//            {
//                mode = ActAuth.Mode.CODE
//            }
//            else
//            {
//                mode = ActAuth.Mode.PHONE
//            }
//            bs_mode.onNext(mode)
        }
    }
}