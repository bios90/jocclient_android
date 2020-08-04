package com.justordercompany.client.base.data_binding

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.justordercompany.client.R
import com.justordercompany.client.extensions.dp2pxInt
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

@BindingAdapter(value = ["my_divider_size", "my_divider_color"],requireAll = true)
fun setDivider(lal: LinearLayout, my_divider_size: Float, my_divider_color: Int)
{
    val drw = GradientDrawable()
    drw.shape = GradientDrawable.RECTANGLE
    if (lal.orientation == LinearLayout.VERTICAL)
    {
        drw.setSize(0, dp2pxInt(my_divider_size))
    }
    else
    {
        drw.setSize(dp2pxInt(my_divider_size), 0)
    }
    drw.setColor(getColorMy(my_divider_color))
    lal.dividerDrawable = drw
}

