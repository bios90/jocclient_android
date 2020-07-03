package com.justordercompany.client.extensions

import android.animation.Animator
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.justordercompany.client.R
import com.justordercompany.client.ui.custom_views.FlipCardAnimation
import io.reactivex.subjects.BehaviorSubject

//Alert Dialog
fun AlertDialog.makeTransparentBg()
{
    this.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
}

fun TextView.makeTextGradient(colors: ArrayList<Int>)
{
    this.setTextColor(getColorMy(R.color.red))
    val paint = this.paint
    val text_width = paint.measureText(this.text.toString())
    val colors_array = colors.toIntArray()
    val shader = LinearGradient(0f, this.textSize, text_width, 0f, colors_array, null, Shader.TileMode.CLAMP)
    paint.setShader(shader)
}

fun TextView.removeGradient()
{
    this.paint.setShader(null)
}

fun TextView.setTextHtml(text: String)
{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
    {
        this.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
    }
    else
    {
        this.setText(Html.fromHtml(text))
    }
}

fun View.animateFadeOut(duration: Int = 500, force_alpha_zero: Boolean = true)
{
    if (force_alpha_zero)
    {
        this.alpha = 0f
    }

    this.animate().alpha(1f).setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener
            {
                override fun onAnimationRepeat(animation: Animator?)
                {
                }

                override fun onAnimationEnd(animation: Animator?)
                {
                }

                override fun onAnimationCancel(animation: Animator?)
                {
                }

                override fun onAnimationStart(animation: Animator?)
                {
                    this@animateFadeOut.visibility = View.VISIBLE
                }
            })
            .setInterpolator(LinearInterpolator()).start()
}

fun View.animateFadeIn(duration: Int = 500, visibility: Int = View.GONE)
{
    this.animate().alpha(0f).setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener
            {
                override fun onAnimationRepeat(animation: Animator?)
                {
                }

                override fun onAnimationEnd(animation: Animator?)
                {
                    this@animateFadeIn.visibility = visibility
                }

                override fun onAnimationCancel(animation: Animator?)
                {
                }

                override fun onAnimationStart(animation: Animator?)
                {
                }
            })
            .setInterpolator(LinearInterpolator()).start()
}

fun View.animateFlip(action_on_flip: () -> Unit)
{
    val center_y = this.height / 2f
    val center_x = this.width / 2f
    val animation = FlipCardAnimation(0f, 180f, center_x, center_y)
    animation.setDuration(250)
    animation.setFillAfter(false)

    animation.setOnContentChangeListener(
        {
            action_on_flip()
        })

    this.startAnimation(animation)
}

fun CheckBox.getBs(): BehaviorSubject<Boolean>
{
    val bs: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(this.isChecked)

    this.setOnCheckedChangeListener(
        { cheb, is_checked ->

            bs.onNext(is_checked)
        })

    return bs
}
