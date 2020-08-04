package com.justordercompany.client.ui.screens.act_order_dialog

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearSnapHelper
import com.aigestudio.wheelpicker.WheelPicker
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.BusMainEvents
import com.justordercompany.client.base.Constants
import com.justordercompany.client.databinding.ActOrderDialogBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.*
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.ui.screens.act_product_setting.VmActProductSetting
import java.lang.RuntimeException
import java.util.*

class ActOrderDialog : BaseActivity()
{
    lateinit var bnd_order_dailog: ActOrderDialogBinding
    lateinit var vm_order_dailog: VmActOrderDialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        applySliderBottom()
        super.onCreate(savedInstanceState)
        bnd_order_dailog = DataBindingUtil.setContentView(this, R.layout.act_order_dialog)
        getActivityComponent().inject(this)
        vm_order_dailog = my_vm_factory.getViewModel(VmActOrderDialog::class.java)
        setBaseVmActions(vm_order_dailog)

        setSpinners()
        setEvents()
        setListeners()
    }

    private fun setSpinners()
    {
        val strs_hours = (0 until 24).map({ it.toString() })
        val strs_minutes = (0 until 60).map({ it.toString() })

        bnd_order_dailog.spinHours.data = strs_hours
        bnd_order_dailog.spinMinutes.data = strs_minutes
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.white)
        is_light_nav_bar = false
    }

    fun setListeners()
    {
        bnd_order_dailog.tvMakeOrder.setOnClickListener(
            {
                vm_order_dailog.ViewListener().clickedOrder()
            })

        bnd_order_dailog.etComment
                .getBsText()
                .subscribe(
                    {
                        vm_order_dailog.bs_comment.onNext(it)
                    })
                .disposeBy(composite_diposable)


        val listener = object : WheelPicker.OnWheelChangeListener
        {
            override fun onWheelSelected(position: Int)
            {
                val minutes = bnd_order_dailog.spinMinutes.selectedItemPosition
                val hours = bnd_order_dailog.spinHours.selectedItemPosition
                val date = Date().setHoursMy(hours).setMinutesMy(minutes)
                vm_order_dailog.bs_time.onNext(date)
            }

            override fun onWheelScrollStateChanged(state: Int)
            {
            }

            override fun onWheelScrolled(offset: Int)
            {
            }
        }

        bnd_order_dailog.spinMinutes.setOnWheelChangeListener(listener)
        bnd_order_dailog.spinHours.setOnWheelChangeListener(listener)
    }

    fun setEvents()
    {
        vm_order_dailog.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        bindCafe(it)
                    })
                .disposeBy(composite_diposable)

        BasketManager.bs_items
                .mainThreaded()
                .subscribe(
                    {
                        bnd_order_dailog.tvSum.text = BasketManager.getSumText()
                    })
                .disposeBy(composite_diposable)

        vm_order_dailog.bs_time
                .mainThreaded()
                .subscribe(
                    {
                        if (it.getHour() != bnd_order_dailog.spinHours.selectedItemPosition)
                        {
                            bnd_order_dailog.spinHours.setSelectedItemPosition(it.getHour(), false)
                        }

                        if (it.getMinute() != bnd_order_dailog.spinMinutes.selectedItemPosition)
                        {
                            bnd_order_dailog.spinMinutes.setSelectedItemPosition(it.getMinute(), false)
                        }
                    })
                .disposeBy(composite_diposable)
    }

    private fun bindCafe(cafe: ModelCafe)
    {
        cafe.logo?.url_l?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_order_dailog.imgCafeLogo)
            })

        bnd_order_dailog.tvCafeName.text = cafe.name
        bnd_order_dailog.tvAdress.text = cafe.address
        bnd_order_dailog.tvTime.text = cafe.working_hours_str
    }
}