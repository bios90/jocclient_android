package com.justordercompany.client.ui.screens.act_filter

import android.os.Bundle
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import com.innovattic.rangeseekbar.RangeSeekBar
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeCafe
import com.justordercompany.client.databinding.ActFilterBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.FilterData
import java.util.concurrent.TimeUnit

class ActFilter : BaseActivity()
{
    lateinit var vm_act_filter: VmActFilter
    lateinit var bnd_act_filter: ActFilterBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_filter = DataBindingUtil.setContentView(this, R.layout.act_filter)
        getActivityComponent().inject(this)
        vm_act_filter = my_vm_factory.getViewModel(VmActFilter::class.java)
        setEvents()
        setListeners()
        setBaseVmActions(vm_act_filter)

        checkExtra()
    }

    fun setNavStatus()
    {

    }

    fun setEvents()
    {
        vm_act_filter.bs_min_max
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_filter.tvPriceMin.text = "${it.first} р."
                        bnd_act_filter.tvPriceMax.text = "${it.second} р."

                        val pos_left = it.first / 10
                        if(bnd_act_filter.rangePrices.getMinThumbValue() != pos_left)
                        {
                            bnd_act_filter.rangePrices.setMinThumbValue(pos_left)
                        }

                        val pos_right = it.second / 10
                        if(bnd_act_filter.rangePrices.getMaxThumbValue() != pos_right)
                        {
                            bnd_act_filter.rangePrices.setMaxThumbValue(pos_right)
                        }
                    })
                .disposeBy(composite_diposable)

        vm_act_filter.bs_cafe_type
                .mainThreaded()
                .subscribe(
                    {
                        val pos = it.getRadioPos()
                        if (bnd_act_filter.rgCafeType.getCheckedPosition() != pos)
                        {
                            bnd_act_filter.rgCafeType.setCheckedAtPosition(pos)
                        }

                    })
                .disposeBy(composite_diposable)

        vm_act_filter.bs_rating
                .mainThreaded()
                .throttleLast(100,TimeUnit.MILLISECONDS)
                .subscribe(
                    {
                        if (bnd_act_filter.ratingBar.rating != it)
                        {
                            bnd_act_filter.ratingBar.rating = it
                        }

                        bnd_act_filter.tvRating.text = it.toString()
                    })
                .disposeBy(composite_diposable)
    }

    fun setListeners()
    {
        bnd_act_filter.rangePrices.seekBarChangeListener = object : RangeSeekBar.SeekBarChangeListener
        {
            override fun onStartedSeeking()
            {
            }

            override fun onStoppedSeeking()
            {

            }

            override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int)
            {
                vm_act_filter.bs_min_max.onNext(Pair(minThumbValue * 10, maxThumbValue * 10))
            }
        }

        bnd_act_filter.rgCafeType.setOnCheckedChangeListener(
            { rg, checked_id ->

                val checked_pos = rg.getCheckedPosition() ?: return@setOnCheckedChangeListener
                val type = TypeCafe.initFromRadioPos(checked_pos)
                vm_act_filter.bs_cafe_type.onNext(type)
            })


        bnd_act_filter.ratingBar.setOnRatingChangeListener(
            { rating_bar, rating, from_user ->

                vm_act_filter.bs_rating.onNext(rating)
            })

        bnd_act_filter.tvOk.setOnClickListener(
            {
                vm_act_filter.ViewListener().clickedOk()
            })

        bnd_act_filter.tvCancel.setOnClickListener(
            {
                vm_act_filter.ViewListener().clickedCancel()
            })
    }

    private fun checkExtra()
    {
        val extra_filter = intent.getSerializableExtra(Constants.Extras.EXTRA_FILTER) as? FilterData
        vm_act_filter.bs_filter_from_extra.onNext(extra_filter.asOptional())
    }
}