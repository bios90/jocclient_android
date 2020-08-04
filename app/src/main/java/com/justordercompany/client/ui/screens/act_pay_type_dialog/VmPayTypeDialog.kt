package com.justordercompany.client.ui.screens.act_pay_type_dialog

import android.content.Intent
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.logic.utils.builders.BuilderIntent

class VmPayTypeDialog : BaseViewModel()
{
    init
    {
        AppClass.app_component.inject(this)
    }

    inner class ViewListener:ActPayTypeDialogListener
    {
        override fun clickedNewCard()
        {
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_PAY_WITH_CARD,true)
            ps_to_finish.onNext(return_intent.asOptional())
        }

        override fun clickedGooglePlay()
        {
            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.EXTRA_PAY_WITH_GOOGLE_PAY,true)
            ps_to_finish.onNext(return_intent.asOptional())
        }
    }
}