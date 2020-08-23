package com.justordercompany.client.extensions

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.logic.responses.BaseResponse
import com.justordercompany.client.logic.responses.getError
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.networking.NoInternetException
import com.justordercompany.client.networking.ParsingError
import com.justordercompany.client.networking.ServerError
import com.justordercompany.client.networking.UnknownServerError
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception

fun Response<ResponseBody>?.getBodyAsStr(): String?
{
    if (this?.code() == 200)
    {
        return this.body()?.string()
    }

    return this?.errorBody()?.string()
}



fun <T> String?.toObjOrNullGson(obj_class: Class<T>): T?
{
    Log.e("Parse_Trying", "Will parse to ${obj_class.name} string \n\n\n$this\n\n\n")
    val gson = AppClass.gson
    try
    {
        if (this?.equals("null") == true || this?.equals("\"null\"") == true)
        {
            return null
        }

        return gson.fromJson(this, obj_class)
    }
    catch (e: Exception)
    {
        e.printStackTrace()
        return null
    }
}

fun <T> Observable<T>.addProgress(base_vm: BaseViewModel): Observable<T>
{
    return this
            .doOnSubscribe(
                {
                    base_vm.ps_show_hide_progress.onNext(true)
                })
            .doFinally(
                {
                    base_vm.ps_show_hide_progress.onNext(false)
                })
}

fun <T> Observable<T>.addScreenDisabling(base_vm: BaseViewModel): Observable<T>
{
    return this
            .doOnSubscribe(
                {
                    base_vm.ps_enable_disable_screen_touches.onNext(true)
                })
            .doFinally(
                {

                    base_vm.ps_enable_disable_screen_touches.onNext(false)

                })
}

fun <T> Observable<T>.addErrorCatcher(base_vm: BaseViewModel): Observable<T>
{
    return this
            .doOnError(
                {

                    var message = "Errrarara"

                    if (it is ServerError
                            || it is NoInternetException
                            || it is ParsingError
                            || it is UnknownServerError)
                    {
                        if (!it.message.isNullOrEmpty())
                        {
                            message = it.message!!
                        }
                    }

                    val builder_alerter = BuilderAlerter.getRedBuilder(message)
                    base_vm.ps_to_show_alerter.onNext(builder_alerter)
                })
}

fun <T> Observable<T>.addParseChecker(action: (T) -> Boolean): Observable<T>
{
    return this.map(
        {
            if (action(it))
            {
                return@map it
            }

            throw ParsingError()
        })
}

fun <T> Observable<T>.subscribeMy(action_success: (T) -> Unit,action_error: ((Throwable) -> Unit)? = null): Disposable
{
    return this.subscribe(action_success,
        {
            action_error?.invoke(it)
            it.printStackTrace()
            Log.e("subscribeMy", "**** Got error in subscribeMy ****")
        })
}

fun RemoteMessage.getInt(key:String):Int?
{
    val data = this.getData() as Map<String, String>
    return data.get(key)?.toIntOrNull()
}

fun RemoteMessage.getString(key:String):String?
{
    val data = this.getData() as Map<String, String>
    return data.get(key)
}


