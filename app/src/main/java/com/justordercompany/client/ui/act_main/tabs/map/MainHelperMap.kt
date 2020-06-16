package com.justordercompany.client.ui.act_main.tabs.map

import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainMapBinding
import com.justordercompany.client.extensions.moveCameraToPos
import com.justordercompany.client.ui.act_main.ActMain
import com.justordercompany.client.ui.act_main.tabs.MainHelper

class MainHelperMap(val act_main: ActMain) : MainHelper
{
    val bnd_map: LaMainMapBinding
    val vm_main_map:VmMainMap

    lateinit var frag_map :SupportMapFragment
    lateinit var google_map:GoogleMap

    init
    {
        bnd_map = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_map, null, false)
        vm_main_map = act_main.my_vm_factory.getViewModel(VmMainMap::class.java)
        initMap()
    }

    override fun getView(): View
    {
        return bnd_map.root
    }

    fun initMap()
    {
        frag_map = act_main.supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment
        frag_map.getMapAsync(
            {
                google_map = it
//                this.google_map.setMyLocationEnabled(true)
                this.google_map.getUiSettings().setMapToolbarEnabled(false)
                this.google_map.getUiSettings().setMyLocationButtonEnabled(false)
                google_map.moveCameraToPos(LatLng(55.7558, 37.6173))
            })
    }
}