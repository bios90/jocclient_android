package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.PmSortDirection
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiOrders
{
    @FormUrlEncoded
    @POST(Constants.Urls.URL_ORDER_CREATE)
    fun createOrder(
            @Field("date") date: String,
            @Field("comment") comment: String?,
            @Field("items") items: String
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @POST(Constants.Urls.URL_ORDER_PAY)
    fun makePay(
            @Path("id") id: Int,
            @Field("token") token_payment: String
    ): Observable<Response<ResponseBody>>

    @GET(Constants.Urls.URL_ORDER_GET_INFO)
    fun getOrderInfo(@Path("id") id: Int): Observable<Response<ResponseBody>>

    @GET(Constants.Urls.URL_ORDER_GET_USER_ORDERS)
    fun getUserOrders(
            @Query("pagexxx") page: Int
//            @Query("limit") limit: Int = Constants.COUNT_ADD_ON_LOAD,
//            @Query("sort") sort: String = "createdAt",
//            @Query("sort_direction") sort_direction: String = PmSortDirection.DESC.getServerStr(),
//            @Query("status") status: String? = null
    ): Observable<Response<ResponseBody>>
}