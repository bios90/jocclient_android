package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiAuth
{
    @FormUrlEncoded
    @POST(Constants.Urls.URL_REGISTER)
    fun makeRegister(@Field("password") email: String):Observable<Response<ResponseBody>>
}