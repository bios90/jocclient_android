package com.justordercompany.client.base.data_binding

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.view.View
import java.lang.RuntimeException
import android.util.Log
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
    private var is_gradient: Boolean = false
    private var grad_colors: List<Int> = arrayListOf()
    private var grad_orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT

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

    fun isGradient(is_gradient: Boolean): BuilderBg
    {
        this.is_gradient = is_gradient
        return this
    }

    fun setGradColors(colors: List<Int>): BuilderBg
    {
        this.grad_colors = colors
        return this
    }

    fun setGradOrientation(orientation: GradientDrawable.Orientation): BuilderBg
    {
        this.grad_orientation = orientation
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

        if (is_gradient)
        {
            drawable.setColors(grad_colors.toIntArray())
            drawable.orientation = grad_orientation
        }

        if (is_ripple)
        {
//            val is_trans = bg_color == getColorMy(R.color.transparent)
            val drawable_ripple = getRippleDrawable(ripple_color, ripple_color, drawable, corner_radius, true)
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

        @JvmStatic
        fun getRounded4White(): Drawable
        {
            return BuilderBg()
                    .setBgColor(getColorMy(R.color.white))
                    .setCorners(4f)
                    .isDpMode(true)
                    .get()
        }

        @JvmStatic
        fun getSimpleDrawable(radius: Float, color: Int): Drawable
        {
            return BuilderBg()
                    .setBgColor(getColorMy(color))
                    .setCorners(radius)
                    .isDpMode(true)
                    .get()
        }

        @JvmStatic
        fun getEmptyOrange(radius: Float): Drawable
        {
            return BuilderBg()
                    .setBgColor(getColorMy(R.color.transparent))
                    .setStrokeColor(getColorMy(R.color.orange))
                    .setStrokeWidth(2f)
                    .setCorners(radius)
                    .isDpMode(true)
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.orange))
                    .get()
        }

        @JvmStatic
        fun getGradOrange(radius: Float = 4f, orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BL_TR): Drawable
        {
            val colors = arrayListOf(getColorMy(R.color.orange_dark), getColorMy(R.color.orange_light))
            return BuilderBg()
                    .isGradient(true)
                    .setGradOrientation(orientation)
                    .setGradColors(colors)
                    .setCorners(radius)
                    .isDpMode(true)
                    .isRipple(true)
                    .setRippleColor(getColorMy(R.color.gray6))
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

fun getRippleDrawable(color_normal: Int, color_pressed: Int, inner_drawable: Drawable, corner_radius: Float, is_trans: Boolean): RippleDrawable
{
    val corners = FloatArray(8, { corner_radius })
    val drawbale_mask = GradientDrawable()
    drawbale_mask.setColor(color_normal)
    drawbale_mask.cornerRadius = corner_radius
    drawbale_mask.cornerRadii = corners
    (drawbale_mask.mutate() as GradientDrawable).setCornerRadii(corners)

    val colors_list: ColorStateList = getStateColorList(color_normal, color_pressed)
    return RippleDrawable(colors_list, inner_drawable, drawbale_mask)
}