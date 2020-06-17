package com.justordercompany.client.logic.utils.builders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.R
import com.justordercompany.client.extensions.getColorMy
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener

class BuilderAlerter
{
    companion object
    {
        fun getRedBuilder(text:String,title:String = ""):BuilderAlerter
        {
            return BuilderAlerter()
                    .setColor(getColorMy(R.color.red))
                    .setText(text)
                    .setTitle(title)
        }

        fun getGreenBuilder(text:String,title:String = ""):BuilderAlerter
        {
            return BuilderAlerter()
                    .setColor(getColorMy(R.color.green))
                    .setText(text)
                    .setTitle(title)
        }
    }

    private var title: String = ""
    private var text: String = ""
    private var color: Int = getColorMy(R.color.orange)
    private var style_title = R.style.AlerterTitle
    private var style_text = R.style.AlerterText
    private var duration: Int = 4000

    fun setTitle(title: String) = apply(
        {
            this.title = title
        })

    fun setText(text: String) = apply(
        {
            this.text = text
        })

    fun setColor(color: Int) = apply(
        {
            this.color = color
        })

    fun setTitleStyle(style: Int) = apply(
        {
            this.style_title = style
        })

    fun setTextStyle(style: Int) = apply(
        {
            this.style_text = style
        })

    fun setDuration(duration: Int) = apply(
        {
            this.duration = duration
        })

    fun show(activity: AppCompatActivity)
    {
        if (Alerter.isShowing)
        {
            return
        }

        val decor_view = activity.window.decorView
        val def_visibility = decor_view.getSystemUiVisibility()
        decor_view.setSystemUiVisibility(decor_view.getSystemUiVisibility() and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv());

        Alerter.create(activity)
                .setTitle(title)
                .setText(text)
                .setDuration(duration.toLong())
                .setBackgroundColorInt(color)
                .setTextAppearance(style_text)
                .setTitleAppearance(style_title)
                .setOnClickListener(View.OnClickListener(
                    {
                        Alerter.hide()
                        decor_view.setSystemUiVisibility(def_visibility)
                    }))
                .setOnHideListener(OnHideAlertListener(
                    {
                        decor_view.setSystemUiVisibility(def_visibility)
                    }))
                .show()
    }
}