package com.justordercompany.client.extensions

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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