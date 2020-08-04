package com.justordercompany.client.ui.screens.act_main.tabs.profile

import android.text.SpannedString
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.justordercompany.client.R
import com.justordercompany.client.base.enums.TypeAuthMode
import com.justordercompany.client.databinding.LaMainProfileBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelUser
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.logic.utils.strings.MySpan
import com.justordercompany.client.ui.screens.act_main.ActMain
import com.justordercompany.client.ui.screens.act_main.tabs.TabView
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.justordercompany.client.base.adapters.AdapterRvOrders


class TabProfile(val act_main: ActMain) : TabView
{
    val composite_disposable = act_main.composite_diposable
    val bnd_profile: LaMainProfileBinding
    val vm_tab_profile: VmTabProfile
    val adapter = AdapterRvOrders()

    init
    {
        bnd_profile = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_main_profile, null, false)
        vm_tab_profile = act_main.my_vm_factory.getViewModel(VmTabProfile::class.java)

        setEvents()
        setListeners()
        setOrderRecycler()
        act_main.setBaseVmActions(vm_tab_profile)
        act_main.lifecycle.addObserver(MyLifecycleObserver())

        bnd_profile.larAuthDialog.tvOffert.text = getOffertText()
        bnd_profile.viewFakeStatus.setHeight(getStatusBarHeight())
        bnd_profile.larTop.setHeight(dp2pxInt(212) + getStatusBarHeight())
    }

    fun setOrderRecycler()
    {
        bnd_profile.recOrders.adapter = adapter
        bnd_profile.recOrders.layoutManager = LinearLayoutManager(act_main)
        bnd_profile.recOrders.addDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
        bnd_profile.srlOrders.setColorSchemeResources(R.color.orange_dark, R.color.orange, R.color.orange_light)
    }

    fun setListeners()
    {
        bnd_profile.larAuthDialog.tvSend.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedAuthLogin()
            })

        bnd_profile.larAuthDialog.tvOffert.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedOffertRules()
            })

        connectBoth(bnd_profile.larAuthDialog.etPhone.getBsText(), vm_tab_profile.bs_phone, composite_disposable)
        connectBoth(bnd_profile.larAuthDialog.etCode.getBsText(), vm_tab_profile.bs_code, composite_disposable)
        connectBoth(bnd_profile.larAuthDialog.chOffert.getBs(), vm_tab_profile.bs_offert_checked, composite_disposable)

        bnd_profile.tvEdit.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedEditUser()
            })

        bnd_profile.srlOrders.setOnRefreshListener(
            {
                vm_tab_profile.ViewListener().swipedToRefresh()
            })

    }

    fun setEvents()
    {
        vm_tab_profile.ps_auth_dialog_visibility
                .mainThreaded()
                .subscribe(
                    {

                        Log.e("TabProfile", "setEvents: Got here $it")
                        if (it)
                        {
                            bnd_profile.larAuthDialog.root.animateFadeOut()
                        }
                        else
                        {
                            bnd_profile.larAuthDialog.root.animateFadeIn()
                        }
                    }).disposeBy(composite_disposable)

        vm_tab_profile.bs_auth_mode.subscribe(
            { mode ->

                val action =
                        {
                            if (mode == TypeAuthMode.CODE)
                            {
                                bnd_profile.larAuthDialog.laCode.visibility = View.VISIBLE
                                bnd_profile.larAuthDialog.laPhone.visibility = View.GONE
                                bnd_profile.larAuthDialog.tvSend.text = getStringMy(R.string.confirm)
                            }
                            else
                            {
                                bnd_profile.larAuthDialog.laCode.visibility = View.GONE
                                bnd_profile.larAuthDialog.laPhone.visibility = View.VISIBLE
                                bnd_profile.larAuthDialog.tvSend.text = getStringMy(R.string.send_code)
                            }
                        }

                bnd_profile.larAuthDialog.lalAnim.animateFlip(action)
            })
                .disposeBy(composite_disposable)

        vm_tab_profile.bs_user_to_display
                .subscribe(
                    {
                        val user = it.value ?: return@subscribe
                        bindUser(user)
                    })
                .disposeBy(composite_disposable)

        vm_tab_profile.bs_orders
                .mainThreaded()
                .subscribe(
                    {
                        bnd_profile.srlOrders.isRefreshing = false
                        adapter.setItems(it)
                    })
                .disposeBy(composite_disposable)
    }

    override fun getView(): View
    {
        return bnd_profile.root
    }

    private fun bindUser(user: ModelUser)
    {
        bnd_profile.tvName.text = "Name later!!"
        bnd_profile.tvPhone.text = user.phone
        user.image?.url_m?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_profile.imgAvatar)
            })
    }

    private fun getOffertText(): SpannedString
    {
        val span_1 = MySpan()
                .setColor(getColorMy(R.color.gray6))
                .setFontRes(R.font.exo_reg)
                .setText("Я согласен с условиями ")

        val span_2 = MySpan()
                .setColor(getColorMy(R.color.gray8))
                .setFontRes(R.font.exo_bold)
                .setText("офферты")

        return TextUtils.concat(span_1.build(), span_2.build()) as SpannedString
    }

    inner class MyLifecycleObserver : LifecycleObserver
    {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart()
        {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop()
        {
        }
    }
}