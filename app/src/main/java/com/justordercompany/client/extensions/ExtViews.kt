package com.justordercompany.client.extensions

import android.animation.Animator
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.ui.custom_views.FlipCardAnimation
import io.reactivex.subjects.BehaviorSubject
import android.view.ViewGroup
import com.justordercompany.client.R
import android.view.MotionEvent
import android.os.SystemClock
import android.graphics.*
import android.util.DisplayMetrics
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


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

fun RadioGroup.setCheckedAtPosition(position: Int)
{
    (this.getChildAt(position) as RadioButton).isChecked = true
}

fun RadioGroup.getCheckedPosition(): Int?
{
    if (this.childCount == 0)
    {
        return null
    }

    for (a in 0 until this.childCount)
    {
        val rb = this.getChildAt(a) as RadioButton
        if (rb.isChecked)
        {
            return a
        }
    }

    return null
}

fun View.toBitmap(): Bitmap
{
    this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val bitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(),
        Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight())
    this.draw(canvas)
    return bitmap
}

fun View.setHeight(heightToSet: Int)
{
    this.getLayoutParams().height = heightToSet
    this.requestLayout()
}

fun getStatusBarHeight(): Int
{
    var result = 0
    val resources = AppClass.app.resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
    {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun View.setMargins(l: Int, t: Int, r: Int, b: Int)
{
    if (this.getLayoutParams() is ViewGroup.MarginLayoutParams)
    {
        val p = this.getLayoutParams() as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        this.requestLayout()
    }
}

fun getDrawableMy(id: Int): Drawable
{
    return ContextCompat.getDrawable(AppClass.app, id)!!
}

fun getRippleDrawableMy(id: Int): RippleDrawable
{
    return ContextCompat.getDrawable(AppClass.app, id) as RippleDrawable
}

fun View.simulateSwipeLeft()
{
    val downTime = SystemClock.uptimeMillis()
    val x = 500.0f
    val y = 0.0f
    val metaState = 0

    val event_down = MotionEvent.obtain(
        downTime,
        downTime + 10,
        MotionEvent.ACTION_DOWN,
        x,
        y,
        metaState
    )

    val event_move = MotionEvent.obtain(
        downTime + 10,
        downTime + 100,
        MotionEvent.ACTION_MOVE,
        x - 250,
        y,
        metaState
    )

    val event_up = MotionEvent.obtain(
        downTime + 110,
        downTime + 120,
        MotionEvent.ACTION_UP,
        x - 250,
        y,
        metaState
    )

    this.dispatchTouchEvent(event_down)
    this.dispatchTouchEvent(event_move)
    this.dispatchTouchEvent(event_up)
}

fun View.simulateSwipeRight()
{
    val downTime = SystemClock.uptimeMillis()
    val x = 0.0f
    val y = 0.0f
    val metaState = 0

    val event_down = MotionEvent.obtain(
        downTime,
        downTime + 10,
        MotionEvent.ACTION_DOWN,
        x,
        y,
        metaState
    )

    val event_move = MotionEvent.obtain(
        downTime + 10,
        downTime + 100,
        MotionEvent.ACTION_MOVE,
        x + 250,
        y,
        metaState
    )

    val event_up = MotionEvent.obtain(
        downTime + 110,
        downTime + 120,
        MotionEvent.ACTION_UP,
        x + 250,
        y,
        metaState
    )

    this.dispatchTouchEvent(event_down)
    this.dispatchTouchEvent(event_move)
    this.dispatchTouchEvent(event_up)
}

fun getScreenWidth(): Int
{
    val displayMetrics = DisplayMetrics()
    val window_manager = AppClass.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    window_manager.getDefaultDisplay().getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    return width
}

fun getScreenHeight(): Int
{
    val displayMetrics = DisplayMetrics()
    val window_manager = AppClass.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    window_manager.getDefaultDisplay().getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    return height
}

fun View.isVisibleOnScreen(): Boolean
{
    if (!this.isShown())
    {
        return false
    }
    val actualPosition = Rect()
    this.getGlobalVisibleRect(actualPosition)
    val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
    return actualPosition.intersect(screen)
}

fun RecyclerView.addDivider(color: Int, size: Int, orientation: Int = DividerItemDecoration.VERTICAL)
{
    val drw = GradientDrawable()
    drw.shape = GradientDrawable.RECTANGLE

    val itemDecorator: DividerItemDecoration
    if (orientation == DividerItemDecoration.VERTICAL)
    {
        drw.setSize(0, size)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    }
    else
    {
        drw.setSize(size, 0)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
    }

    drw.setColor(color)

    itemDecorator.setDrawable(drw)
    this.addItemDecoration(itemDecorator)
}


