package com.justordercompany.client.ui.screens.act_review_dialog

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.databinding.ActReviewDialogBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.utils.DateManager
import com.justordercompany.client.logic.utils.formatAsMoney
import com.justordercompany.client.logic.utils.formatAsRating
import com.justordercompany.client.logic.utils.formatToString
import io.reactivex.subjects.BehaviorSubject
import java.lang.RuntimeException

class ActReviewDialog : BaseActivity()
{
    lateinit var vm_review_dialog: VmActReviewDialog
    lateinit var bnd_act_review_dialog: ActReviewDialogBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        bnd_act_review_dialog = DataBindingUtil.setContentView(this, R.layout.act_review_dialog)
        vm_review_dialog = my_vm_factory.getViewModel(VmActReviewDialog::class.java)
        setBaseVmActions(vm_review_dialog)
        checkExtra()

        setListeners()
        setEvents()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_nav_bar = true
    }

    fun setEvents()
    {
        vm_review_dialog.bs_order
                .mainThreaded()
                .subscribe(
                    {
                        bindOrder(it)
                    })
                .disposeBy(composite_diposable)

        vm_review_dialog.bs_rating
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_review_dialog.tvRating.text = it.toInt().toString()
                    })
                .disposeBy(composite_diposable)

        vm_review_dialog.bs_cafe_name
                .mainThreaded()
                .subscribe(
                    {
                        bindCafeName(it)
                    })
                .disposeBy(composite_diposable)
    }

    fun setListeners()
    {
        connectBoth(bnd_act_review_dialog.etEt.getBsText(), vm_review_dialog.bs_review_text, composite_diposable)
        bnd_act_review_dialog.ratingBar.setOnRatingChangeListener(
            { rb, rating, from_user ->

                vm_review_dialog.bs_rating.onNext(rating.toDouble())
            })

        bnd_act_review_dialog.tvOk.setOnClickListener(
            {
                vm_review_dialog.ViewListener().clickedOk()
            })

        bnd_act_review_dialog.tvCancel.setOnClickListener(
            {
                vm_review_dialog.ViewListener().clickedCancel()
            })
    }

    private fun bindOrder(order: ModelOrder)
    {
        val title = "Заказ ${order.id}"
        val text = "${order.cafe?.name} - ${order.date?.formatToString(DateManager.FORMAT_FOR_DISPLAY_WITH_TIME)} - ${order.cafe?.address} - ${order.sum?.formatAsMoney()} р."

        bnd_act_review_dialog.tvTitle.text = title
        bnd_act_review_dialog.tvText.text = text
    }

    private fun bindCafeName(name: String)
    {
        val title = getStringMy(R.string.review)
        val text = getString(R.string.review_about_cafe) + " $name"

        bnd_act_review_dialog.tvTitle.text = title
        bnd_act_review_dialog.tvText.text = text
    }

    private fun checkExtra()
    {
        val order_id = intent.getIntExtraMy(Constants.Extras.EXTRA_ORDER_ID)
        val cafe_id = intent.getIntExtraMy(Constants.Extras.EXTRA_CAFE_ID)

        if (order_id != null)
        {
            vm_review_dialog.bs_order_id.onNext(order_id)
        }
        if (cafe_id != null)
        {
            vm_review_dialog.bs_cafe_id.onNext(cafe_id)
        }
    }
}