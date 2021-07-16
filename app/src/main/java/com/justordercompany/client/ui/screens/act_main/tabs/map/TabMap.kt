package com.justordercompany.client.ui.screens.act_main.tabs.map

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.justordercompany.client.R
import com.justordercompany.client.base.data_binding.BuilderBg
import com.justordercompany.client.base.enums.TypeCafeStatus
import com.justordercompany.client.databinding.LaMainMapBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.LocationManager
import com.justordercompany.client.logic.utils.PermissionManager
import com.justordercompany.client.ui.dialogs.DialogBottomCafe
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabMap(val act_main: ActMain) : TabView
{
    val composite_disposable = act_main.composite_diposable
    val bnd_map: LaMainMapBinding
    val vm_tab_map: VmTabMap

    lateinit var frag_map: SupportMapFragment
    lateinit var google_map: GoogleMap
    var current_markers: ArrayList<Marker> = arrayListOf()
    var current_route: Polyline? = null

    init
    {
        bnd_map = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_map, null, false)
        vm_tab_map = act_main.my_vm_factory.getViewModel(VmTabMap::class.java)
        act_main.setBaseVmActions(vm_tab_map)

        setEvents()
        setListeners()
        initMap()

        bnd_map.tvLegend.setMargins(null, getStatusBarHeight() + dp2pxInt(62f), null, null)
    }

    fun setListeners()
    {
        bnd_map.tvClearRoute.setOnClickListener(
            {
                vm_tab_map.ViewListener().clickedClearRoute()
            })

        bnd_map.tvLegend.setOnClickListener(
            {
                vm_tab_map.ViewListener().clickedLegend()
            })
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

        vm_tab_map.bs_cafe_to_display
                .mainThreaded()
                .subscribe(
                    {
                        bindCafes(it)
                    })
                .disposeBy(composite_disposable)

        vm_tab_map.bs_cafe_bottom_dialog
                .mainThreaded()
                .subscribe(
                    {
                        val dialog = DialogBottomCafe()
                        dialog.cafe = it
                        dialog.show(this.act_main.supportFragmentManager, null)
                    })
                .disposeBy(composite_disposable)

        vm_tab_map.ps_to_show_route
                .mainThreaded()
                .subscribe(
                    {
                        clearRoute()
                        if (it.value != null)
                        {
                            bnd_map.tvClearRoute.animateFadeOut()
                            current_route = google_map.addPolyline(it.value)
                        }
                        else
                        {
                            bnd_map.tvClearRoute.animateFadeIn()
                        }
                    })
                .disposeBy(composite_disposable)

        vm_tab_map.ps_to_show_legend
                .mainThreaded()
                .subscribe(
                    {
                        act_main.messages_manager.showLegendDialog()
                    })
                .disposeBy(composite_disposable)
    }

    override fun getView(): View
    {
        return bnd_map.root
    }

    @SuppressLint("MissingPermission")
    fun initMap()
    {
        frag_map = act_main.supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment
        frag_map.getMapAsync(
            {
                google_map = it

                this.google_map.getUiSettings().setMapToolbarEnabled(false)
                this.google_map.getUiSettings().setMyLocationButtonEnabled(true)
                val padding = dp2px(0f).toInt()
                this.google_map.setPadding(padding, padding + getStatusBarHeight(), padding, padding)

                val pos_to_move = SharedPrefsManager.getLastLatLng()?.toLatLng() ?: LatLng(55.751244, 37.618423)
                google_map.moveCameraToPos(pos_to_move)
                setMapListener()

                act_main.permissions_manager.checkAndRequest(PermissionManager.permissions_location)
                        .subscribe(
                            {
                                this.google_map.isMyLocationEnabled = true
//                                this.google_map.uiSettings.isMyLocationButtonEnabled = true
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

        google_map.setOnMarkerClickListener(
            {
                val cafe = it.tag as? ModelCafe
                if (cafe != null)
                {
                    vm_tab_map.ViewListener().clickedCafe(cafe)
                    return@setOnMarkerClickListener true
                }

                return@setOnMarkerClickListener false
            })

    }

    private fun bindCafes(cafes: ArrayList<ModelCafe>)
    {
        clearMarkers()

        for (cafe in cafes)
        {
            if (cafe.lat == null || cafe.lon == null || cafe.name == null)
            {
                continue
            }

            val view = act_main.layoutInflater.inflate(R.layout.la_marker, null, false)
            val tv: TextView = view.findViewById(R.id.tv_cafe_name)
            val arrow: ImageView = view.findViewById(R.id.img_arrow)

            Log.e("TabMap", "bindCafes: Status is "+cafe.status)
            val color = cafe.status?.getColorId() ?: R.color.biryza
            tv.background = BuilderBg.getSimpleDrawable(999f, color)
            arrow.setColorFilter(getColorMy(color))

            tv.text = cafe.name
            val bitmap = view.toBitmap()
            val options = MarkerOptions()
                    .position(LatLng(cafe.lat!!, cafe.lon!!))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

            val marker = google_map.addMarker(options)
            marker.tag = cafe
            current_markers.add(marker)
        }
    }

    private fun clearMarkers()
    {
        for (marker in current_markers)
        {
            marker.remove()
        }

        current_markers.clear()
    }

    private fun clearRoute()
    {
        current_route?.remove()
        current_route = null
    }
}