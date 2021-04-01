package com.justordercompany.client.logic.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import com.justordercompany.client.R
import com.justordercompany.client.extensions.applyTransparency
import com.justordercompany.client.extensions.getColorMy
import java.io.Serializable
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object RoutesManager
{
    fun getRouteInfo(start: LatLng, finish: LatLng): RouteInfoMy?
    {
        val from_str = "${start.latitude},${start.longitude}"
        val to_str = "${finish.latitude},${finish.longitude}"

        val geo_context = GeoApiContext.Builder()
                .apiKey("AIzaSyDGvaukwLZyj3jvJrWQ7fAFBxMHOWJKS2U")
                .build()

        val req = DirectionsApi.newRequest(geo_context)
                .mode(TravelMode.WALKING)
                .alternatives(true)
//                .trafficModel(TrafficModel.OPTIMISTIC)
//                .transitRoutingPreference(TransitRoutingPreference.FEWER_TRANSFERS)
                .destination(to_str)
                .origin(from_str)

//        val req = DirectionsApi.getDirections(geo_context, from_str, to_str);

        val route_points: ArrayList<LatLng> = arrayListOf()
        var time: Long? = null
        var distance: Long? = null

        try
        {
            val directions_result = req.await()

            val route_legs = getShortestLegRoute(directions_result)

            time = route_legs?.duration?.inSeconds
            distance = route_legs?.distance?.inMeters

            val steps = route_legs?.steps
            steps?.forEach(
                {
                    val decoded_points = it.polyline.decodePath()
                            .map(
                                {
                                    LatLng(it.lat, it.lng)
                                })

                    route_points.addAll(decoded_points)
                })
        }
        catch (e: Exception)
        {
            return null
        }

        if (route_points.size > 0 && time != null && distance != null)
        {
            val routes_map = route_points.map(
                {
                    return@map Pair(it.latitude, it.longitude)
                })
                    .toCollection(ArrayList())


            return RouteInfoMy(routes_map, time, distance)
        }
        else
        {
            return null
        }
    }

    private fun getShortestLegRoute(directions_result: DirectionsResult): DirectionsLeg?
    {
        val map_of_lengts: TreeMap<Long, Pair<Int, Int>> = TreeMap()

        directions_result.routes.forEachIndexed(
            { route_index, route ->

                route.legs.forEachIndexed(
                    { route_legs_index, route_legs ->

                        val distance = route_legs.distance.inMeters
                        map_of_lengts.put(distance, Pair(route_index, route_legs_index))
                    })
            })

        val best = map_of_lengts.firstEntry()
        if (best != null)
        {
            return directions_result.routes.get(best.value.first).legs.get(best.value.second)
        }
        else
        {
            return null
        }
    }
}

class RouteInfoMy(
        val points_pairs: ArrayList<Pair<Double, Double>>,
        val time_in_seconds: Long,
        val distance_in_meters: Long
) : Serializable
{
    fun getPoints(): ArrayList<LatLng>
    {
        return points_pairs.map(
            {
                LatLng(it.first, it.second)
            })
                .toCollection(ArrayList())
    }

    fun getPolyInfo(): PolylineOptions
    {
        val opts = PolylineOptions().addAll(getPoints()).color(getColorMy(R.color.orange).applyTransparency(75)).width(10f)
        return opts
    }
}