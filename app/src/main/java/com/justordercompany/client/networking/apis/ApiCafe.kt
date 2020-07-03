package com.justordercompany.client.networking.apis

import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.PmCafeSort
import com.justordercompany.client.base.enums.PmSortDirection
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCafe
{
    @GET(Constants.Urls.URL_GET_CAFE)
    fun getCafes(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("distance") distance: Int,
            @Query("order") sort_by: PmCafeSort,
            @Query("order_direction") sort_direction: PmSortDirection
            ): Observable<Response<ResponseBody>>
}