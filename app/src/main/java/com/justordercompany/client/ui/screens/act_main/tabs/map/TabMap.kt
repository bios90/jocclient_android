package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.justordercompany.client.R
import com.justordercompany.client.databinding.LaMainMapBinding
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getVisibleDistance
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.extensions.moveCameraToPos
import com.justordercompany.client.logic.utils.PermissionManager
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.ActMainTab

class TabMap(val act_main: ActMain) : ActMainTab
{
    val composite_disposable = act_main.composite_diposable
    val bnd_map: LaMainMapBinding
    val vm_tab_map: VmTabMap

    lateinit var frag_map: SupportMapFragment
    lateinit var google_map: GoogleMap

    init
    {
        bnd_map = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_map, null, false)
        vm_tab_map = act_main.my_vm_factory.getViewModel(VmTabMap::class.java)
        act_main.setBaseVmActions(vm_tab_map)

        setEvents()
        setListeners()
        initMap()
    }

    fun setListeners()
    {

    }

    fun setEvents()
    {
        vm_tab_map.ps_move_map_pos
                .mainThreaded()
                .subscribe(
                    {
                        google_map.moveCameraToPos(it)
                    })
                .disposeBy(composite_disposable)
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

                this.google_map.getUiSettings().setMapToolbarEnabled(false)
                this.google_map.getUiSettings().setMyLocationButtonEnabled(false)
                google_map.moveCameraToPos(LatLng(55.7558, 37.6173))
                setMapListener()

                act_main.permissions_manager.checkAndRequest(PermissionManager.permissions_location)
                        .subscribe(
                            {
                                this.google_map.isMyLocationEnabled = true
                                this.google_map.uiSettings.isMyLocationButtonEnabled = true
                            },
                            {
                                it.printStackTrace()
                            })
                        .disposeBy(composite_disposable)

                vm_tab_map.ViewListener().mapInited()
            })
    }

    private fun setMapListener()
    {
        google_map.setOnCameraIdleListener(
            {
                val distance = google_map.getVisibleDistance()
                vm_tab_map.bs_current_visible_distance.onNext(distance)

                val center = google_map.cameraPosition.target
                vm_tab_map.bs_current_center.onNext(center)

                vm_tab_map.ViewListener().mapIdled()
            })
    }
}