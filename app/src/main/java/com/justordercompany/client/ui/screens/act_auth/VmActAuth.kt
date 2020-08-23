package com.justordercompany.client.ui.screens.act_auth

import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeAuthMode
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.getNullString
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.ValidationManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import io.reactivex.subjects.BehaviorSubject

class VmActAuth : BaseViewModel()
{
    val bs_auth_mode: BehaviorSubject<TypeAuthMode> = BehaviorSubject.createDefault(TypeAuthMode.PHONE)
    val bs_phone: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_code: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_offert_checked: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)


    init
    {
        AppClass.app_component.inject(this)
    }

    inner class ViewListener:ActAuthListener
    {
        override fun clickedAuthLogin()
        {
            when (bs_auth_mode.value)
            {
                TypeAuthMode.PHONE ->
                {
                    makePhoneSend()
                }

                TypeAuthMode.CODE ->
                {
                    makeCodeSend()
                }
            }
        }

        private fun makePhoneSend()
        {
            val phone = bs_phone.value?.value

            if (phone == null)
            {
                val text = getStringMy(R.string.please_phone)
                ps_to_show_alerter.onNext(BuilderAlerter.getRedBuilder(text))
                return
            }

            val fb_token = SharedPrefsManager.getString(SharedPrefsManager.Key.FB_TOKEN)

            base_networker.makeAuth(phone, fb_token,
                {
                    bs_auth_mode.onNext(TypeAuthMode.CODE)
                })
        }

        private fun makeCodeSend()
        {
            val code = bs_code.value?.value
            val phone = bs_phone.value?.value
            val is_checked = bs_offert_checked.value

            if (!ValidationManager.validateCodeSend(code, phone, is_checked))
            {
                val errors = ValidationManager.getCodeSendErrors(code, phone, is_checked)
                ps_to_show_alerter.onNext(BuilderAlerter.getRedBuilder(errors))
                return
            }

            base_networker.makeCodeConfirm(phone!!, code!!,
                {

                    SharedPrefsManager.saveUser(it)
                    bus_main_events.ps_user_logged.onNext(Any())
                    ps_to_finish.onNext(Optional(null))
                })
        }


        override fun clickedOffertRules()
        {
            val btn_ok = BtnAction(getStringMy(R.string.its_clear), null)
            val text = getStringMy(R.string.offert) ?: return
            val builder = BuilderDialogMy()
                    .setText(text)
                    .setBtnOk(btn_ok)
                    .setViewId(R.layout.la_dialog_scrollable_tv)
                    .setIsHtml(true)
            ps_to_show_dialog.onNext(builder)
        }
    }
}