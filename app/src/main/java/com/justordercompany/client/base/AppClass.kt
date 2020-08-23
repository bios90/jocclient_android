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
        val fb_token = SharedPrefsManager.getString(SharedPrefsManager.Key.FB_TOKEN)
        if(fb_token != null)
        {
            Log.e("FbMessagingService", "test: fb token is $fb_token")
        }
    }
}