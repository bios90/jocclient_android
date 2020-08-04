package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_categ

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.*
import com.justordercompany.client.base.enums.TypeProduct
import com.justordercompany.client.extensions.disposeBy
import com.justordercompany.client.extensions.getBoolExtraMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.models.ModelBasketItem
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.models.ModelProduct
import com.justordercompany.client.logic.models.getProductsOfType
import com.justordercompany.client.logic.utils.BasketManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_cafe_popup.ActCafePopup
import com.justordercompany.client.ui.screens.act_product_setting.ActProductSetting
import io.reactivex.subjects.BehaviorSubject

class VmTabCateg : BaseViewModel()
{
    var bs_cafe: BehaviorSubject<ModelCafe> = BehaviorSubject.create()
    var bs_type: BehaviorSubject<TypeProduct> = BehaviorSubject.create()
    var bs_products_to_display: BehaviorSubject<FeedDisplayInfo<ModelProduct>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    private fun setEvents()
    {
        bs_cafe.subscribe(
            {
                val type = bs_type.value ?: return@subscribe

                val products = it.getProductsOfType(type)

                if (products != null && products.size > 0)
                {
                    bs_products_to_display.onNext(FeedDisplayInfo(products, LoadBehavior.UPDATE))
                }
            })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener : TabCategListener
    {
        override fun clickedProduct(product: ModelProduct)
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActProductSetting::class.java)
                    .addParam(Constants.Extras.EXTRA_PRODUCT, product)
                    .setOkAction(
                        {
                            val item = it?.getSerializableExtra(Constants.Extras.EXTRA_BASKET_ITEM) as? ModelBasketItem
                            if (item != null)
                            {
                                BasketManager.addItem(item)
                                val builder = BuilderAlerter.getGreenBuilder(getStringMy(R.string.product_added_to_basket))
                                ps_to_show_alerter.onNext(builder)
                            }
                        })
                    .setSlider(BuilderIntent.TypeSlider.BOTTOM_UP)

            ps_intent_builded.onNext(builder)
        }
    }
}