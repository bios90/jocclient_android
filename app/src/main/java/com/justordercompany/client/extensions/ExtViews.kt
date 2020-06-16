package com.justordercompany.client.extensions

import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.justordercompany.client.R

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
