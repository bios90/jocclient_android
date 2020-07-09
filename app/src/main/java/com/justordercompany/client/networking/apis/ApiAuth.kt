package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiAuth
{
    @FormUrlEncoded
    @POST(Constants.Urls.URL_REGISTER)
    fun makeAuth(
            @Field("phone") phone: String,
            @Field("push_token") push_token: String?
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT(Constants.Urls.URL_PHONE_CONFIRM)
    fun confirmPhone(
            @Field("phone") phone: String,
            @Field("code") code: String
    ): Observable<Response<ResponseBody>>

//
//    @Multipart
//    @PUT(Constants.Urls.URL_USER_UPDATE)
//    fun updateUserInfo(
//            @PartMap params: Map<String, String?>,
//            @Part image: MultipartBody.Part?
//    ): Observable<Response<ResponseBody>>
//
//    @PUT(Constants.Urls.URL_USER_UPDATE)
//    fun updateUserInfo2(@Body form_data: RequestBody): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT(Constants.Urls.URL_USER_UPDATE)
    fun updateUserInfo(
            @Field("name") first_name: String?,
            @Field("email") email: String?,
            @Field("push_token") push_token: String?,
            @Field("image") image_as_64: String?)
            : Observable<Response<ResponseBody>>

    @GET(Constants.Urls.URL_USER_INFO)
    fun getUser(): Observable<Response<ResponseBody>>
}