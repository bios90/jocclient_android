package com.justordercompany.client.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class ViewPagerNotScrollable : ViewPager
{
    var scrollEnabled = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent?): Boolean
    {
        if (this.scrollEnabled)
        {
            return super.onTouchEvent(ev)
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
    {
        if (this.scrollEnabled)
        {
            return super.onInterceptTouchEvent(ev);
        }

        return false;
    }
}