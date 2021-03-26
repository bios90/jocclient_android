package com.justordercompany.client.ui.custom_views

import android.content.Context
import android.text.Editable
import android.text.TextUtils
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
        var text_listener: TextWatcher? = null
        text_listener = object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                this@MyEtWithObserv.removeTextChangedListener(text_listener)
                var text: String? = s.toString().trim()
                if (TextUtils.isEmpty(text))
                {
                    text = null
                }
                br_text.acceptIfNotMatches(text.asOptional())
                this@MyEtWithObserv.addTextChangedListener(text_listener)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
            }
        }
        this.addTextChangedListener(text_listener)

        br_text
                .mainThreaded()
                .distinctUntilChanged()
                .subscribe(
                    {
                        this@MyEtWithObserv.removeTextChangedListener(text_listener)
                        this.acceptIfNotMatches(it.value)
                        this@MyEtWithObserv.addTextChangedListener(text_listener)
                    })
                .disposeBy(composite_disposable)
    }

    fun getBsText(): BehaviorSubject<Optional<String>>
    {
        return br_text
    }
}