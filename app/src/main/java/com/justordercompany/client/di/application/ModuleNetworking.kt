package com.justordercompany.client.di.application

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.justordercompany.client.R
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.networking.MyInterceptor
import com.justordercompany.client.networking.apis.ApiAuth
import com.justordercompany.client.networking.apis.ApiCafe
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

@Module
class ModuleNetworking
{
    @Singleton
    @Provides
    fun getGson(): Gson
    {
        val gson = GsonBuilder()
                .setDateFormat(getStringMy(R.string.format_for_server))
                .create()

        return gson
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: MyInterceptor,log_interceptor: HttpLoggingInterceptor): OkHttpClient
    {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(interceptor)
        httpClientBuilder.addInterceptor(log_interceptor)
        httpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor
    {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun provideMyInterceptor():MyInterceptor
    {
        return MyInterceptor()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson,client:OkHttpClient):Retrofit
    {
        return Retrofit.Builder()
                .baseUrl(Constants.Urls.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Singleton
    @Provides
    fun provideApiAuth(retrofit: Retrofit):ApiAuth
    {
        return retrofit.create(ApiAuth::class.java)
    }

    @Singleton
    @Provides
    fun provideApiCafes(retrofit: Retrofit):ApiCafe
    {
        return retrofit.create(ApiCafe::class.java)
    }

}




















