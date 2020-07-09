package com.justordercompany.client.logic.requests

import com.justordercompany.client.base.enums.PmCafeSort
import com.justordercompany.client.base.enums.PmSortDirection
import com.justordercompany.client.logic.models.FilterData
import com.justordercompany.client.networking.apis.ApiAuth
import com.justordercompany.client.networking.apis.ApiCafe
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.RuntimeException

class ReqCafes
{
    var lat: Double? = null
    var lon: Double? = null
    var distance: Int? = null
    var filter: FilterData? = null
    var order: PmCafeSort = PmCafeSort.DISTANCE
    var sort: PmSortDirection = PmSortDirection.ASC

    fun getRequest(api_cafe: ApiCafe): Observable<Response<ResponseBody>>
    {
        if (lat == null || lon == null || distance == null)
        {
            throw RuntimeException("**** Error no params ****")
        }

        return api_cafe.getCafes(
            lat!!,
            lon!!,
            distance!!,
            filter?.price_min,
            filter?.price_max,
            filter?.rating,
            order.getNameForRequest(),
            sort.name)
    }
}