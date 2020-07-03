package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiAuth
{
    @FormUrlEncoded
    @POST(Constants.Urls.URL_REGISTER)
    fun makeAuth(
            @Field("phone") phone: String,
            @Field("push_token") push_token:String
    ):Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT(Constants.Urls.URL_PHONE_CONFIRM)
    fun confirmPhone(
            @Field("phone") phone: String,
            @Field("code") code:String
    ):Observable<Response<ResponseBody>>
}