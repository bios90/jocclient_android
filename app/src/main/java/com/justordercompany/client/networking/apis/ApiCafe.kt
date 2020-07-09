package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCafe
{
    @GET(Constants.Urls.URL_GET_CAFES)
    fun getCafes(
            @Query("lat") lat: Double,
            @Query("lng") lon: Double,
            @Query("distance") distance: Int,
            @Query("min_price") min_price: Int?,
            @Query("max_price") max_price: Int?,
        //Todo change later for right
            @Query("ratingggg") rating: Float?,
            @Query("order") sort_by: String,
            @Query("order_direction") sort_direction: String
    ): Observable<Response<ResponseBody>>

    @GET(Constants.Urls.URL_GET_CAFE_SINGLE)
    fun getCafeSingle(
            @Path("id") id: Int
    ): Observable<Response<ResponseBody>>
}