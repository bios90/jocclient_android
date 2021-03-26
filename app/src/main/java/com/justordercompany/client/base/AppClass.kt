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
import com.justordercompany.client.extensions.toJsonMy
import com.justordercompany.client.extensions.toObjOrNullGson
import com.justordercompany.client.logic.models.ModelUser


//User real geo moving!!

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
//        fakeLogin()
    }

    fun test()
    {
        val user = SharedPrefsManager.getCurrentUser().toJsonMy()
        Log.e("AppClass", "test: user is $user")
    }

    fun fakeLogin()
    {
        val user_str = "{\"api_token\":\"90A9F5YJNp9WT3ndP0xCi4Vw7IxPB7Jf\",\"email\":\"\",\"image\":{\"height\":3024,\"src\":\"https://dev.justordercompany.com/media/cache/resolve/api_avatar_src/uploads/client/5218ba90f7030cb498620a2548407fb1.jpeg\",\"url_l\":\"https://dev.justordercompany.com/media/cache/api_avatar_l/uploads/client/5218ba90f7030cb498620a2548407fb1.jpeg\",\"url_m\":\"https://dev.justordercompany.com/media/cache/api_avatar_m/uploads/client/5218ba90f7030cb498620a2548407fb1.jpeg\",\"url_s\":\"https://dev.justordercompany.com/media/cache/api_avatar_s/uploads/client/5218ba90f7030cb498620a2548407fb1.jpeg\",\"width\":3024},\"phone\":\"79167062291\"}"
        val user = user_str.toObjOrNullGson(ModelUser::class.java)
        SharedPrefsManager.saveUser(user!!)
    }
}