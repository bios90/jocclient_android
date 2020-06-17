package com.justordercompany.client.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.logic.utils.getRandomString
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

fun runActionWithDelay(delay: Int, action: () -> Unit, error_action: (() -> Unit)? = null): Disposable
{
    return Observable.timer(delay.toLong(), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action()
                },
                {
                    error_action?.invoke()
                })
}

fun runRepeatingAction(interval: Int, action: () -> Unit, max_repeat: Int = 40): Disposable
{
    return Observable.interval(interval.toLong(), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .take(max_repeat.toLong())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action()
                })
}

fun runActionOnMainThread(action: () -> Unit): Disposable
{
    return Single.just(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action()
                },
                {
                    Log.e("Errr", "runActionOnMainThread: error")
                })
}

fun Toast(text: String = String.getRandomString())
{
    android.widget.Toast.makeText(AppClass.app, text, android.widget.Toast.LENGTH_LONG).show()
}

fun getRandomColor(): Int
{
    val rnd = Random()
    val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    return color
}

fun getColorMy(id: Int): Int
{
    return ContextCompat.getColor(AppClass.app, id)
}

fun dp2pxInt(dp: Int): Int
{
    return dp2px(dp.toFloat()).toInt()
}

fun dp2pxInt(dp: Float): Int
{
    return dp2px(dp).toInt()
}

fun dp2px(dp: Float): Float
{
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
}

fun getStringMy(id: Int): String
{
    return AppClass.app.getResources().getString(id)
}

fun getStringMy(id: Int, text: String): String
{
    return AppClass.app.getResources().getString(id, text)
}

fun getStringMy(id: Int, vararg values: Any): String
{
    return AppClass.app.getResources().getString(id, *values)
}

fun openAppSettings()
{
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.data = Uri.parse("package:" + AppClass.app.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    AppClass.app.startActivity(intent)
}

fun isNetworkAvailable(): Boolean
{
    val connectivityManager = AppClass.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when
        {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
    else
    {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}