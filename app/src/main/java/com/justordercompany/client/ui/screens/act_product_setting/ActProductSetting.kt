package com.justordercompany.client.ui.screens.act_product_setting

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.databinding.ActProductSettingBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.*
import com.justordercompany.client.logic.utils.formatAsMoney
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.ui.custom_views.bubble_view.BubbleView
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class ActProductSetting : BaseActivity()
{
    lateinit var vm_product_setting: VmActProductSetting
    lateinit var bnd_product_setting: ActProductSettingBinding

    var bubble_view_weights: BubbleView? = null
    var bubble_view_sugar: BubbleView? = null
    var bubble_view_milks: BubbleView? = null
    var bubble_view_addables: BubbleView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        applySliderBottom()
        bnd_product_setting = DataBindingUtil.setContentView(this, R.layout.act_product_setting)
        getActivityComponent().inject(this)
        vm_product_setting = my_vm_factory.getViewModel(VmActProductSetting::class.java)

        setTopMargin()
        setEvents()
        setListeners()
        setBaseVmActions(vm_product_setting)
        checkExtras()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.white)
        is_light_nav_bar = false
    }

    fun setTopMargin()
    {
        val margin = getStatusBarHeight() + dp2px(50f)
        bnd_product_setting.viewRoot.setMargins(0, margin.toInt(), 0, dp2pxInt(-12f))
    }

    fun setEvents()
    {
        vm_product_setting.bs_product
                .mainThreaded()
                .subscribe(
                    {
                        bindProduct(it)
                    })
                .disposeBy(composite_diposable)

        vm_product_setting.bs_busket_item
                .mainThreaded()
                .subscribe(
                    {
                        bindBasketItem(it)
                    })
                .disposeBy(composite_diposable)
    }

    fun setListeners()
    {
        bnd_product_setting.tvAdd.setOnClickListener(
            {
                vm_product_setting.ViewListener().clickedAdd()
            })
    }

    private fun bindProduct(product: ModelProduct)
    {
        product.image?.url_l?.let(
            {
                GlideManager.loadImageSimple(it, bnd_product_setting.imgProduct)
            })

        bnd_product_setting.tvName.text = product.name
        bnd_product_setting.tvDescription.text = product.description

        setWeights(product)
        if (product.type == TypeProduct.HOT || product.type == TypeProduct.COLD)
        {
            setSugars()
        }
        setMilks(product)
        setAddables(product)
    }

    private fun setWeights(product: ModelProduct)
    {
        if (!product.hasWeights())
        {
            return
        }

        bubble_view_weights = BubbleView(this.layoutInflater, bnd_product_setting.lalForAddables)
        bubble_view_weights!!.vm_bubble.bs_addables.onNext(product.weights!!)
        bubble_view_weights!!.vm_bubble.bs_title_text.onNext(getStringMy(R.string.weight))
        bnd_product_setting.lalForAddables.addView(bubble_view_weights!!.getView())

        bubble_view_weights!!.vm_bubble.bs_selected_pos
                .subscribe(
                    {
                        val weight = product.weights!!.getValuesByPoses(it).getOrNull(0) ?: return@subscribe
                        vm_product_setting.ViewListener().weightChangedTo(weight)
                    })
                .disposeBy(composite_diposable)
    }

    private fun setSugars()
    {
        val sugars = ModelAddableValue.getAddableSugar()

        bubble_view_sugar = BubbleView(this.layoutInflater, bnd_product_setting.lalForAddables)
        bubble_view_sugar!!.vm_bubble.bs_addables.onNext(sugars)
        bubble_view_sugar!!.vm_bubble.bs_title_text.onNext(getStringMy(R.string.sugar))
        bnd_product_setting.lalForAddables.addView(bubble_view_sugar!!.getView())

        bubble_view_sugar!!.vm_bubble.bs_selected_pos
                .subscribe(
                    {
                        val pos = it.getOrNull(0) ?: return@subscribe
                        vm_product_setting.ViewListener().sugarChangedTo(pos)
                    })
                .disposeBy(composite_diposable)
    }

    private fun setAddables(product: ModelProduct)
    {
        if (!product.hasAddables())
        {
            return
        }

        bubble_view_addables = BubbleView(this.layoutInflater, bnd_product_setting.lalForAddables)
        bubble_view_addables!!.vm_bubble.bs_addables.onNext(product.addables!!)
        bubble_view_addables!!.vm_bubble.bs_title_text.onNext(getStringMy(R.string.addables))
        bubble_view_addables!!.vm_bubble.bs_multi_selectable.onNext(true)
        bnd_product_setting.lalForAddables.addView(bubble_view_addables!!.getView())

        bubble_view_addables!!.vm_bubble.bs_selected_pos
                .subscribe(
                    {
                        val addables = product.addables!!.getValuesByPoses(it)
                        vm_product_setting.ViewListener().addablesChangedTo(addables)
                    })
                .disposeBy(composite_diposable)
    }

    private fun setMilks(product: ModelProduct)
    {
        if (!product.hasMilks())
        {
            return
        }

        bubble_view_milks = BubbleView(this.layoutInflater, bnd_product_setting.lalForAddables)
        bubble_view_milks!!.vm_bubble.bs_addables.onNext(product.milks!!)
        bubble_view_milks!!.vm_bubble.bs_title_text.onNext(getStringMy(R.string.milk))
        bnd_product_setting.lalForAddables.addView(bubble_view_milks!!.getView())

        bubble_view_milks!!.vm_bubble.bs_selected_pos
                .subscribe(
                    {
                        val milk = product.milks!!.getValuesByPoses(it).getOrNull(0) ?: return@subscribe
                        vm_product_setting.ViewListener().milkChangedTo(milk)
                    })
                .disposeBy(composite_diposable)
    }

    private fun bindBasketItem(item: ModelBasketItem)
    {
        item.weight?.let(
            { weight ->

                val pos = item.product?.weights?.indexOf(weight)
                Log.e("ActProductSetting", "bindBasketItem: pos is $pos")
                if (pos != null && bubble_view_weights != null)
                {
                    Log.e("ActProductSetting", "Will bind weights selected poses ")
                    bubble_view_weights!!.vm_bubble.bs_selected_pos.onNext(arrayListOf(pos))

                    val price = arrayListOf(weight).getPriceSum()
                    val price_text = "${price.formatAsMoney()} р."
                    bubble_view_weights!!.vm_bubble.bs_price_text.onNext(price_text)
                }
                else
                {
                    Log.e("ActProductSetting", "bindBasketItem: Bubble view is null((")
                }
            })

        item.sugar?.let(
            { sugar ->

                if (bubble_view_sugar != null)
                {
                    bubble_view_sugar!!.vm_bubble.bs_selected_pos.onNext(arrayListOf(sugar))
                    val price_text = "${0.formatAsMoney()} р."
                    bubble_view_sugar!!.vm_bubble.bs_price_text.onNext(price_text)
                }
            })

        item.milk?.let(
            { milk ->

                val pos = item.product?.milks?.indexOf(milk)
                if (pos != null && bubble_view_milks != null)
                {
                    bubble_view_milks!!.vm_bubble.bs_selected_pos.onNext(arrayListOf(pos))

                    val price = arrayListOf(milk).getPriceSum()
                    val price_text = "${price.formatAsMoney()} р."
                    bubble_view_milks!!.vm_bubble.bs_price_text.onNext(price_text)
                }
            })

        item.addables?.let(
            {
                Log.e("ActProductSetting", "bindBasketItem: Addables not and count is ${it.size}")
                val poses = item.product?.getPosesOfAddables(it)?.toCollection(ArrayList())
                Log.e("ActProductSetting", "bindBasketItem: Poses are $poses")
                if (bubble_view_addables != null && poses != null)
                {
                    bubble_view_addables!!.vm_bubble.bs_selected_pos.onNext(poses)
                    val price = it.getPriceSum()
                    val price_text = "${price.formatAsMoney()} р."
                    bubble_view_addables!!.vm_bubble.bs_price_text.onNext(price_text)
                }
            })

        val price_text = "${item.getSum().formatAsMoney()} р."
        bnd_product_setting.tvSum.text = price_text
    }

    fun checkExtras()
    {
        val basket_item = intent.getSerializableExtra(Constants.Extras.EXTRA_BASKET_ITEM) as? ModelBasketItem
        var product = basket_item?.product

        if (product == null)
        {
            product = intent.getSerializableExtra(Constants.Extras.EXTRA_PRODUCT) as? ModelProduct
        }

        if (product == null)
        {
            throw RuntimeException("**** Errr no product passed ****")
        }

        vm_product_setting.bs_product.onNext(product)

        if (basket_item != null)
        {
            runActionWithDelay(100,
                {
                    vm_product_setting.bs_busket_item.onNext(basket_item)
                })
        }
    }
}