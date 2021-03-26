package com.justordercompany.client.ui.screens.act_intro_slides

import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.logic.models.ModelSlideData
import io.reactivex.subjects.BehaviorSubject

class VmActIntroSlides : BaseViewModel()
{
    val bs_slides: BehaviorSubject<ArrayList<ModelSlideData>>

    init
    {
        bs_slides = BehaviorSubject.createDefault(ModelSlideData.getSlidesData())
    }

    inner class ViewListener
    {
        fun clickedStartSkip()
        {
            ps_to_finish.onNext(Optional(null))
        }
    }
}