package com.justordercompany.client.ui.screens.act_info_dialog

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.databinding.ActFilterBinding
import com.justordercompany.client.databinding.LaInfoDialogBinding
import com.justordercompany.client.extensions.emailIntent
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.extensions.setTextHtml
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.ui.screens.act_filter.VmActFilter

class ActInfoDialog : BaseActivity()
{
    lateinit var bnd_info_dialog: LaInfoDialogBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_info_dialog = DataBindingUtil.setContentView(this, R.layout.la_info_dialog)
        getActivityComponent().inject(this)

        setListeners()

        bnd_info_dialog.tvOffert.setTextHtml(getStringMy(R.string.offert))
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_nav_bar = true
    }

    private fun setListeners()
    {
        bnd_info_dialog.tvContact.setOnClickListener(
            {
                val user = SharedPrefsManager.getCurrentUser()
                val title = "Письмо из приложения Joc"
                var text = ""
                if(user != null)
                {
                    text+="Пользователь: ${user.name}"
                    text+="\nТелефон: ${user.phone}"
                }

                emailIntent("jocforusers@gmail.con",text,title)
            })
    }
}