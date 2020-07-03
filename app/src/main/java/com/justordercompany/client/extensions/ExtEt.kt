package com.justordercompany.client.extensions

import android.text.TextUtils
import android.widget.EditText

fun EditText.acceptIfNotMatches(text: String?)
{
    val current_et_text = this.getNullableText()

    if (current_et_text == null && text == null)
    {
        return
    }

    if (current_et_text.equals(text))
    {
        return
    }

    this.setText(text)
}

fun EditText.getNullableText(): String?
{
    val text = this.text.toString().trim()
    if (TextUtils.isEmpty(text))
    {
        return null
    }

    return text
}