package com.justordercompany.client.ui.screens.act_intro_slides

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.base.adapters.AdapterVpBase
import com.justordercompany.client.databinding.ActIntroSlidesBinding
import com.justordercompany.client.databinding.LaSlideBinding
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.models.ModelSlideData
import com.justordercompany.client.logic.utils.images.GlideManager

class ActIntroSlides : BaseActivity()
{
    lateinit var vm_act_intro_slides: VmActIntroSlides
    lateinit var bnd_act_intro_slides: ActIntroSlidesBinding
    val adapter = AdapterVpBase()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_intro_slides = DataBindingUtil.setContentView(this, R.layout.act_intro_slides)
        getActivityComponent().inject(this)
        vm_act_intro_slides = my_vm_factory.getViewModel(VmActIntroSlides::class.java)
        setBaseVmActions(vm_act_intro_slides)

        setEvents()
        setListeners()
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

    private fun setEvents()
    {
        vm_act_intro_slides.bs_slides
                .mainThreaded()
                .subscribe(
                    {

                        val views: ArrayList<View> = arrayListOf()
                        it.forEach(
                            {
                                val bnd: LaSlideBinding = DataBindingUtil.inflate(layoutInflater, R.layout.la_slide, null, false)
                                bnd.bindSlide(it)
                                views.add(bnd.root)
                            })
                        Log.e("ActIntroSlides", "setEvents: Views setted ${it.size}")
                        adapter.setViews(views)

                    })
                .disposeBy(composite_diposable)
    }

    private fun setListeners()
    {
        bnd_act_intro_slides.vpVp.adapter = adapter
        bnd_act_intro_slides.dotsIndicator.setViewPager(bnd_act_intro_slides.vpVp)

        bnd_act_intro_slides.tvNext.setOnClickListener(
            {
                bnd_act_intro_slides.vpVp.scrollToNextIfPossible()
            })

        bnd_act_intro_slides.tvSkip.setOnClickListener(
            {
                vm_act_intro_slides.ViewListener().clickedStartSkip()
            })

        bnd_act_intro_slides.tvStart.setOnClickListener(
            {
                vm_act_intro_slides.ViewListener().clickedStartSkip()
            })

        bnd_act_intro_slides.vpVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int)
            {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                if (position == adapter.count - 1)
                {
                    bnd_act_intro_slides.tvSkip.animateFadeIn(300, View.INVISIBLE)
                    bnd_act_intro_slides.tvNext.animateFadeIn(300, View.INVISIBLE)
                    bnd_act_intro_slides.tvStart.animateFadeOut(300, false)
                }
                else
                {
                    bnd_act_intro_slides.tvSkip.animateFadeOut(300, false)
                    bnd_act_intro_slides.tvNext.animateFadeOut(300, false)
                    bnd_act_intro_slides.tvStart.animateFadeIn(300, View.INVISIBLE)
                }
            }
        })
    }
}

fun LaSlideBinding.bindSlide(model_slide: ModelSlideData)
{
    this.tvTitle.text = model_slide.title
    this.tvText.text = model_slide.text
    Glide.with(AppClass.app)
            .load(model_slide.img_res_id)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .transition(DrawableTransitionOptions.withCrossFade(250))
            .into(this.imgImg)
}