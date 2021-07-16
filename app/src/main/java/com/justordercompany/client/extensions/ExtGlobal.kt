package com.justordercompany.client.extensions

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.logic.utils.strings.getRandomString
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
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

fun runRepeatingAction(interval: Int, action: (Int) -> Unit, max_repeat: Int = 40, inital_delay: Int = 0): Disposable
{
    return Observable.interval(inital_delay.toLong(), interval.toLong(), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .take(max_repeat.toLong())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action(it.toInt())
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

fun getDimenMy(id: Int): Float
{
    return AppClass.app.resources.getDimension(id)
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

fun getTypeFaceFromResource(id: Int): Typeface
{
    return ResourcesCompat.getFont(AppClass.app, id)!!
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

fun getStringFromRawRes(res_id: Int): String?
{
    val inputStream: InputStream
    try
    {
        inputStream = AppClass.app.resources.openRawResource(res_id)
    }
    catch (e: Resources.NotFoundException)
    {
        e.printStackTrace()
        return null
    }


    val byteArrayOutputStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var length: Int = inputStream.read(buffer)
    try
    {
        while (length != -1)
        {
            byteArrayOutputStream.write(buffer, 0, length)
        }
    }
    catch (e: IOException)
    {
        e.printStackTrace()
        return null
    }
    finally
    {
        try
        {
            inputStream.close()
            byteArrayOutputStream.close()
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

    }

    val resultString: String
    try
    {
        resultString = byteArrayOutputStream.toString("UTF-8")
    }
    catch (e: UnsupportedEncodingException)
    {
        e.printStackTrace()
        return null
    }

    return resultString
}

fun Any?.toJsonMy(): String?
{
    if (this == null)
    {
        return null
    }

    val json = AppClass.gson.toJson(this)
    if (json.equals("null") || json.equals("\"null\""))
    {
        return null
    }

    return json
}

fun Int.applyTransparency(percent: Int): Int
{
    val alpha = (255 * percent) / 100
    val new_color = ColorUtils.setAlphaComponent(this, alpha)
    return new_color
}

@ColorInt
infix fun @receiver:ColorInt Int.overlay(@ColorInt bottomColor: Int): Int
{
    val topAlpha = Color.alpha(this)
    val bottomAlpha = Color.alpha(bottomColor)
    val alphaSum = bottomAlpha + topAlpha
    return Color.argb(
        Math.min(255, alphaSum),
        (Color.red(bottomColor) * bottomAlpha + Color.red(this) * topAlpha) / alphaSum,
        (Color.green(bottomColor) * bottomAlpha + Color.green(this) * topAlpha) / alphaSum,
        (Color.blue(bottomColor) * bottomAlpha + Color.blue(this) * topAlpha) / alphaSum
    )
}

fun Dialog.setDimAlpha(alpha: Float)
{
    val window = this.getWindow() ?: return
    val params = window.attributes ?: return
    params.dimAmount = alpha
    params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
    window.setAttributes(params)
}

fun Dialog.setNavigationBarColor(color: Int)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val window = this.window ?: return
        val metrics = DisplayMetrics()
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics)

        val dimDrawable = GradientDrawable()

        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(color)

        val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

        val windowBackground = LayerDrawable(layers)

        windowBackground.setLayerInsetTop(1, metrics.heightPixels)

        window.setBackgroundDrawable(windowBackground)
    }
}

fun getCurrentHour(): Int
{
    val time = Calendar.getInstance()
    return time.get(Calendar.HOUR_OF_DAY)
}

fun getCurrentMinute(): Int
{
    val time = Calendar.getInstance()
    return time.get(Calendar.MINUTE)
}

fun Boolean.toVisibility(): Int
{
    if (this)
    {
        return View.VISIBLE
    }
    else
    {
        return View.GONE
    }
}

fun emailIntent(whom: String?, text: String?, subj: String?)
{
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
        "mailto", whom, null))
    intent.putExtra(Intent.EXTRA_SUBJECT, subj)
    intent.putExtra(Intent.EXTRA_TEXT, text);

    val chooser = Intent.createChooser(intent, "Отправить email")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try
    {
        AppClass.app.startActivity(chooser)
    }
    catch (e: Exception)
    {
        e.printStackTrace()
    }
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

fun openUrlIntent(url: String)
{
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setData(Uri.parse(url))

    val chooser = Intent.createChooser(intent, "Открыть")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try
    {
        AppClass.app.startActivity(chooser)
    }
    catch (e: java.lang.Exception)
    {
        e.printStackTrace()
    }
}
