package com.justordercompany.client.extensions

import com.justordercompany.client.networking.NoInternetException
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

fun <T> connectBoth(first: BehaviorSubject<T>, second: BehaviorSubject<T>,cd: CompositeDisposable?)
{
    val disposable_1 =first.distinctUntilChanged()
            .subscribe(
        {
           second.onNext(it)
        })

    val disposable_2 =second.distinctUntilChanged()
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


