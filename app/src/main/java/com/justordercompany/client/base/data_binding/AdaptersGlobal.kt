package com.justordercompany.client.base.data_binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getColorMy

@BindingAdapter("my_stroke_width", "my_stroke_color", "bg_color", "corner_radius", "is_ripple", "ripple_color", requireAll = false)
fun setMyBuilderBg(view: View, my_stroke_width: Float = 0f, my_stroke_color: Int = getColorMy(R.color.transparent), bg_color: Int = getColorMy(R.color.transparent), corner_radius: Float = 0f, is_ripple: Boolean = false, ripple_color: Int = getColorMy(R.color.orange_trans_50))
{
    BuilderBg().setView(view)
            .isDpMode(true)
            .setStrokeWidth(my_stroke_width)
            .setStrokeColor(my_stroke_color)
            .setBgColor(bg_color)
            .setCorners(corner_radius)
            .isRipple(is_ripple)
            .setRippleColor(ripple_color)
            .applyToView()
}
