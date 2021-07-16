package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.justordercompany.client.R
import com.justordercompany.client.base.adapters.AdapterRvReviews
import com.justordercompany.client.base.data_binding.BuilderBg
import com.justordercompany.client.databinding.ItemCafeImageBinding
import com.justordercompany.client.databinding.LaTabCafePageBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelCafe
import com.justordercompany.client.logic.utils.images.GlideManager
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.tabs.TabView

class TabCafePage(val act_cafe_menu: ActCafeMenu) : TabView
{
    val bnd_tab_cafe_page: LaTabCafePageBinding
    val vm_tab_cafe_page: VmTabCafePage
    val composite_disposable = act_cafe_menu.composite_diposable
    val adapter_reviews = AdapterRvReviews()

    init
    {
        bnd_tab_cafe_page = DataBindingUtil.inflate(act_cafe_menu.layoutInflater, R.layout.la_tab_cafe_page, null, false)
        vm_tab_cafe_page = act_cafe_menu.my_vm_factory.getViewModel(VmTabCafePage::class.java)
        act_cafe_menu.setBaseVmActions(vm_tab_cafe_page)

        setListeners()
        setRecycler()
        setEvents()

        bnd_tab_cafe_page.lalDistance.background = BuilderBg()
                .isGradient(false)
                .isRipple(true)
                .setStrokeColor(getColorMy(R.color.white))
                .setStrokeWidth(1f)
                .setCorners(999f)
                .isDpMode(true)
                .setBgColor(getColorMy(R.color.transparent))
                .setRippleColor(getColorMy(R.color.white).applyTransparency(50))
                .get()
    }

    fun setRecycler()
    {
        bnd_tab_cafe_page.recReviews.adapter = adapter_reviews
        bnd_tab_cafe_page.recReviews.layoutManager = LinearLayoutManager(act_cafe_menu)
        bnd_tab_cafe_page.recReviews.addDivider(getColorMy(R.color.transparent), dp2pxInt(8f))

        adapter_reviews.action_card_clicked =
                {

                }
    }

    override fun getView(): View
    {
        return bnd_tab_cafe_page.root
    }

    fun setListeners()
    {
        bnd_tab_cafe_page.imgLogo.setOnClickListener(
            {
                vm_tab_cafe_page.ViewListener().clickedLogo()
            })

        bnd_tab_cafe_page.lalDistance.setOnClickListener(
            {
                vm_tab_cafe_page.ViewListener().clickedDistance()
            })

        bnd_tab_cafe_page.tvAddReply.setOnClickListener(
            {
                vm_tab_cafe_page.ViewListener().clickedAddReview()
            })

        bnd_tab_cafe_page.tvIsFavorite.setOnClickListener(
            {
                vm_tab_cafe_page.ViewListener().clickedToggleFavorite()
            })
    }

    fun setEvents()
    {
        vm_tab_cafe_page.bs_cafe
                .mainThreaded()
                .subscribe(
                    {
                        bindCafe(it)
                    })
                .disposeBy(composite_disposable)

        vm_tab_cafe_page.ps_cafe_distance_text
                .mainThreaded()
                .subscribe(
                    {
                        bnd_tab_cafe_page.tvDistance.text = it
                    })
                .disposeBy(composite_disposable)
    }

    private fun bindCafe(cafe: ModelCafe)
    {
        bnd_tab_cafe_page.tvName.text = cafe.name
        cafe.rating?.let(
            {
                bnd_tab_cafe_page.ratingBar.rating = it
            })
        cafe.logo?.url_l?.let(
            {
                GlideManager.loadImageSimpleCircle(it, bnd_tab_cafe_page.imgLogo)
            })

        bnd_tab_cafe_page.tvIsFavorite.visibility = (SharedPrefsManager.getCurrentUser() != null).toVisibility()
        if (cafe.isFavorite())
        {
            bnd_tab_cafe_page.tvIsFavorite.typeface = getTypeFaceFromResource(R.font.fa_solid)
        }
        else
        {
            bnd_tab_cafe_page.tvIsFavorite.typeface = getTypeFaceFromResource(R.font.fa_regular)
        }

        bnd_tab_cafe_page.ftvText.setTypeface(getTypeFaceFromResource(R.font.exo_reg))
        bnd_tab_cafe_page.ftvText.setTextSize(getDimenMy(R.dimen.size_s))
        bnd_tab_cafe_page.ftvText.setTextColor(getColorMy(R.color.white))
        var cafe_desc = "\n\n\n\n"
        cafe.description?.let(
            {
                cafe_desc += it
            })
        bnd_tab_cafe_page.ftvText.setText(cafe_desc)

        bindCafeImages(cafe)

        bnd_tab_cafe_page.tvAdress.text = cafe.address
        bnd_tab_cafe_page.tvTime.text = cafe.working_hours_str

        if (!cafe.reviews.isNullOrEmpty())
        {
            cafe.reviews!!.reverse()
            adapter_reviews.setItems(cafe.reviews!!)
            bnd_tab_cafe_page.tvNoReviews.visibility = View.GONE
        }
        else
        {
            bnd_tab_cafe_page.tvNoReviews.visibility = View.VISIBLE
        }

        bnd_tab_cafe_page.laSocialIcons.bindSocialIcons(cafe.getSocials())
    }

    private fun bindCafeImages(cafe: ModelCafe)
    {
        if (cafe.images == null || cafe.images!!.size == 0)
        {
            bnd_tab_cafe_page.scrollImages.visibility = View.GONE
            return
        }

        bnd_tab_cafe_page.laForImages.removeAllViews()

        bnd_tab_cafe_page.scrollImages.visibility = View.VISIBLE

        val inflater = act_cafe_menu.layoutInflater
        cafe.images?.forEach(
            { base_img ->
                val uri = base_img.url_m ?: return@forEach
                val view_img: ItemCafeImageBinding = DataBindingUtil.inflate(inflater, R.layout.item_cafe_image, bnd_tab_cafe_page.laForImages, false)
                GlideManager.loadImageSimple(uri, view_img.imgImg)
                bnd_tab_cafe_page.laForImages.addView(view_img.root)

                view_img.root.setOnClickListener(
                    {
                        vm_tab_cafe_page.ViewListener().clickedImage(base_img)
                    })
            })


    }
}
















