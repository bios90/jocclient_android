package com.justordercompany.client.base.data_binding

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import java.lang.RuntimeException
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.widget.TextView
import com.justordercompany.client.R
import com.justordercompany.client.extensions.dp2px
import com.justordercompany.client.extensions.getColorMy


class BuilderBg
{
    private var is_dp_mode: Boolean = false
    private var view: View? = null
    private var stroke_width: Float = 0f
    private var stroke_color: Int = getColorMy(R.color.transparent)
    private var bg_color: Int = getColorMy(R.color.transparent)
    private var corner_radius: Float = 0f
    private var is_ripple: Boolean = false
    private var ripple_color: Int = getColorMy(R.color.orange_trans_50)

    fun setView(view: View): BuilderBg
    {
        this.view = view
        return this
    }

    fun setStrokeWidth(stroke_width: Float): BuilderBg
    {
        this.stroke_width = stroke_width
        return this
    }

    fun setStrokeColor(color: Int): BuilderBg
    {
        this.stroke_color = color
        return this
    }

    fun setBgColor(color: Int): BuilderBg
    {
        this.bg_color = color
        return this
    }

    fun setCorners(radius: Float): BuilderBg
    {
        this.corner_radius = radius
        return this
    }

    fun isRipple(is_ripple: Boolean): BuilderBg
    {
        this.is_ripple = is_ripple
        return this
    }

    fun setRippleColor(color: Int): BuilderBg
    {
        this.ripple_color = color
        return this
    }

    fun isDpMode(is_dp_mode: Boolean): BuilderBg
    {
        this.is_dp_mode = is_dp_mode
        return this
    }

    fun get(): Drawable
    {
        if (is_dp_mode)
        {
            remakeValuesForDpMode()
        }

        val drawable = GradientDrawable()
        drawable.setColor(bg_color)
        drawable.setStroke(stroke_width.toInt(), stroke_color)
        drawable.cornerRadius = corner_radius

        if (is_ripple)
        {
            val drawable_ripple = getRippleDrawable(ripple_color, ripple_color, drawable, corner_radius)
            return drawable_ripple
        }

        return drawable
    }

    fun applyToView()
    {
        if (view == null)
        {
            throw RuntimeException("**** Error view not setted ****")
        }

        view!!.background = get()
    }

    private fun remakeValuesForDpMode()
    {
        stroke_width = dp2px(stroke_width)
        corner_radius = dp2px(corner_radius)
    }

    companion object
    {
        @JvmStatic
        fun getSquareRippleTransOrange(): Drawable
        {
            return BuilderBg()
                    .isDpMode(true)
                    .setBgColor(getColorMy(R.color.transparent))
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.orange_trans_50))
                    .get()
        }
    }
}

fun getStateColorList(color_normal: Int, color_pressed: Int): ColorStateList
{
    return ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_activated),
            intArrayOf())
        , intArrayOf(
            color_pressed,
            color_pressed,
            color_pressed,
            color_normal)
    )
}

fun getRippleDrawable(color_normal: Int, color_pressed: Int, inner_drawable: Drawable, corner_radius: Float): RippleDrawable
{
    val drawbale_mask = GradientDrawable()
    drawbale_mask.setColor(color_normal)
    drawbale_mask.cornerRadius = corner_radius

    val colors_list = getStateColorList(color_normal, color_pressed)
    return RippleDrawable(colors_list, inner_drawable, drawbale_mask)
}