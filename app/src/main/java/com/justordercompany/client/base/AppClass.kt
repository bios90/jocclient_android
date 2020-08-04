package com.justordercompany.client.base

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.justordercompany.client.R
import com.justordercompany.client.di.application.ComponentApplication
import com.justordercompany.client.di.application.DaggerComponentApplication
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.local_data.SharedPrefsManager
import io.reactivex.disposables.CompositeDisposable
import com.google.firebase.internal.FirebaseAppHelper.getToken
import com.google.firebase.iid.InstanceIdResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.extensions.toObjOrNullGson
import com.justordercompany.client.logic.models.ModelUser


class AppClass : Application()
{
    companion object
    {
        lateinit var app: AppClass
        lateinit var app_component: ComponentApplication
        lateinit var gson: Gson
        lateinit var composite_disposable: CompositeDisposable
        var top_activity: AppCompatActivity? = null
    }

    override fun onCreate()
    {
        super.onCreate()

        app = this

        app_component = DaggerComponentApplication.builder()
                .build()
        composite_disposable = CompositeDisposable()

        gson = GsonBuilder()
                .setDateFormat(getStringMy(R.string.format_for_server))
                .create()
        test()
    }

    fun test()
    {
//        val user_str = "{\"first_name\":\"\",\"email\":\"\",\"last_name\":\"\",\"api_token\":\"psgeJ94WvoAAVtULBQBOSiUOAytGy4DS\",\"push_token\":\"fiNlsuQm8tU:APA91bGnu17a6TKQJ0INcxJXPxc7dUZyP-og216LoQ2dQSsl5-0TiJ6xa9x64cyK0nAIpqeBAtL1w0E6nIBKJmwmm5gMOEbplABCuakjszp-yY5KRr5K5v0VHVxwETQJc1es1UQNv1CE\",\"phone\":\"79167062291\",\"created_at\":\"08.07.2020 22:07:36\",\"updated_at\":\"08.07.2020 22:08:15\",\"confirmed\":true,\"image\":null}"
//        val user = user_str.toObjOrNullGson(ModelUser::class.java)!!
//        SharedPrefsManager.saveUser(user)
//        Log.e("AppClass", "test: current fb token is ${SharedPrefsManager.getString(SharedPrefsManager.Key.FB_TOKEN)}")
    }
}