package com.justordercompany.client.ui.screens.act_cafe_menu.tabs.tab_cafe_page

import com.justordercompany.client.logic.models.BaseImage
import com.justordercompany.client.logic.models.ModelReview

interface TabCafePageListener
{
    fun clickedImage(image: BaseImage)
    fun clickedLogo()
    fun clickedDistance()
    fun clickedAddReview()
    fun clickedReview(review: ModelReview)
    fun clickedToggleFavorite()
}