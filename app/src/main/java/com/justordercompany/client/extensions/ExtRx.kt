package com.justordercompany.client.extensions

import android.util.Log
import com.justordercompany.client.networking.NoInternetException
import com.justordercompany.client.networking.ParsingError
import com.justordercompany.client.networking.ServerError
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.ResponseBody
import retrofit2.Response

fun Disposable.disposeBy(cd: CompositeDisposable)
{
    cd.add(this)
}

data class Optional<T>(val value: T?)

fun <T> T?.asOptional() = Optional(this)

fun Completable.mainThreaded(): Completable
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.mainThreaded(): Observable<T>
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.mainThreaded(): Single<T>
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

@Suppress("UNCHECKED_CAST")
fun <T> Observable<Response<ResponseBody>>.addMyParser(obj_class: Class<out Any>): Observable<T>
{
    return this
            .doOnSubscribe(
                {
                    if (!isNetworkAvailable())
                    {
                        throw NoInternetException()
                    }
                })
            .flatMap(
                {
                    val reponse_as_str = it.getBodyAsStr()

                    val error = reponse_as_str?.toObjOrNullGson(ServerError::class.java)
                    if (error != null && error.message != null)
                    {
                        Log.e("throwing", "Will throw SErverError")
                        throw error
                    }
                    else
                    {
                        Log.e("throwing", "Error is null $error")
                    }

                    val obj = reponse_as_str?.toObjOrNullGson(obj_class) as? T

                    if (obj == null)
                    {
                        throw ParsingError()
                    }

                    return@flatMap Observable.just(obj)
                })
}

fun BehaviorSubject<Optional<String>>.acceptIfNotMatches(opt_str: Optional<String>)
{
    val current_br_text = this.value?.value
    val accepted_text = opt_str.value

    if (current_br_text == null && accepted_text == null)
    {
        return
    }

    if (accepted_text.equals(current_br_text))
    {
        return
    }

    this.onNext(opt_str)
}

fun <T> connectBoth(first: BehaviorSubject<T>, second: BehaviorSubject<T>, cd: CompositeDisposable?)
{
    val disposable_1 = first.distinctUntilChanged()
            .subscribe(
                {
                    second.onNext(it)
                })

    val disposable_2 = second.distinctUntilChanged()
            .subscribe(
                {
                    first.onNext(it)
                })

    cd?.let(
        {
            disposable_1.disposeBy(cd)
            disposable_2.disposeBy(cd)
        })
}

fun <T> BehaviorSubject<ArrayList<T>>.addItem(item: T)
{
    var items = this.value
    if (items == null)
    {
        items = arrayListOf()
    }
    items.add(item)
    this.onNext(items)
}

fun <T> BehaviorSubject<ArrayList<T>>.addItems(items_new: List<T>)
{
    var items = this.value
    if (items == null)
    {
        items = arrayListOf()
    }
    items.addAll(items_new)
    this.onNext(items)
}

fun <T> BehaviorSubject<ArrayList<T>>.removeItem(item: T):Boolean
{
    var items = this.value
    if (items == null)
    {
        items = arrayListOf()
    }

    val removed = items.remove(item)
    if(removed)
    {
        this.onNext(items)
    }

    return removed
}

fun <T> BehaviorSubject<ArrayList<T>>.clear()
{
    this.onNext(arrayListOf())
}


