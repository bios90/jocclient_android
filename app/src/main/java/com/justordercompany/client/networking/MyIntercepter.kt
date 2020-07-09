package com.justordercompany.client.networking

import android.util.Log
import com.justordercompany.client.base.Constants
import com.justordercompany.client.local_data.SharedPrefsManager
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor:Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response
    {
        val request = chain.request()
        val url = request.url().newBuilder()
        val builded_url = url.build()
        val newRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .url(builded_url)

        var token  = SharedPrefsManager.getUserToken()
        if(token == null)
        {
            Log.e("MyInterceptor", "intercept: Toke null!!!")
            token = Constants.Urls.BEARER_TOKEN
        }

        newRequest.addHeader("Authorization", "Bearer $token")

        return chain.proceed(newRequest.build())
    }
}