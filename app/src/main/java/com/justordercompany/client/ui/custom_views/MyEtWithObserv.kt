package com.justordercompany.client.ui.custom_views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.justordercompany.client.extensions.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class MyEtWithObserv : EditText
{
    constructor(context: Context) : super(context)
    {
        custom_init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        custom_init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        custom_init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    {
        custom_init()
    }

    val str_dummy: String? = null
    val br_text: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(Optional(str_dummy))
    val composite_disposable = CompositeDisposable()

    private fun custom_init()
    {
        br_text
                .mainThreaded()
                .subscribe(
                    {
                        this.acceptIfNotMatches(it.value)
                    })
                .disposeBy(composite_disposable)

        this.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                val opt = this@MyEtWithObserv.getNullableText().asOptional()
                br_text.acceptIfNotMatches(opt)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        })
    }

    fun getBsText(): BehaviorSubject<Optional<String>>
    {
        return br_text
    }
}