package com.justordercompany.client.logic.utils.strings

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import com.justordercompany.client.extensions.getTypeFaceFromResource

class MySpan()
{
    var text:String = ""
    var text_color:Int? = null
    var typeface:Typeface? = null
    var font_res_id:Int? = null

    fun setText(text:String) = apply(
        {
            this.text = text
        })

    fun setColor(text_color:Int) = apply(
        {
            this.text_color = text_color
        })

    fun setTypeface(typeface:Typeface) = apply(
        {
            this.typeface = typeface
        })

    fun setFontRes(font_res_id:Int) = apply(
        {
            this.font_res_id = font_res_id
        })

    fun build():SpannableString
    {
        if(font_res_id != null)
        {
            typeface = getTypeFaceFromResource(font_res_id!!)
        }

        val span_str = SpannableString(text)

        typeface?.let(
            {
                span_str.setSpan(TypefaceSpan(it), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            })

        text_color?.let(
            {
                span_str.setSpan(ForegroundColorSpan(it), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            })

        return span_str
    }
}