package com.justordercompany.client.logic.utils.strings

import android.graphics.Paint
import android.graphics.Paint.SUBPIXEL_TEXT_FLAG
import android.text.TextPaint
import android.graphics.Typeface
import android.text.style.MetricAffectingSpan


class TypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan()
{
    override fun updateDrawState(tp: TextPaint)
    {
        tp.typeface = typeface
        tp.flags = tp.flags or SUBPIXEL_TEXT_FLAG
    }

    override fun updateMeasureState(p: TextPaint)
    {
        p.typeface = typeface
        p.flags = p.flags or SUBPIXEL_TEXT_FLAG
    }
}