package com.justordercompany.client.ui.screens.act_main.tabs.profile

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeAuthMode
import com.justordercompany.client.extensions.*
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelUser
import com.justordercompany.client.logic.responses.BaseResponse
import com.justordercompany.client.logic.responses.RespUserSingle
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.ValidationManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.networking.apis.ApiAuth
import com.justordercompany.client.ui.screens.act_profile_edit.ActProfileEdit
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VmTabProfile : BaseViewModel()
{
    val ps_auth_dialog_visibility: PublishSubject<Boolean> = PublishSubject.create()
    val bs_auth_mode: BehaviorSubject<TypeAuthMode> = BehaviorSubject.createDefault(TypeAuthMode.PHONE)
    val bs_phone: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_code: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_offert_checked: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val bs_user_to_display: BehaviorSubject<Optional<ModelUser>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setEvents()
    }

    override fun viewAttached()
    {
        checkForLogin()
    }

    fun setEvents()
    {
        bus_main_events.ps_user_logged
                .subscribe(
                    {
                        userLogged()
                    })
                .disposeBy(composite_disposable)

        bus_main_events.ps_user_profile_updated
                .subscribe(
                    {
                        Networking().loadUser(
                            {
                                bs_user_to_display.onNext(it.asOptional())
                            })
                    })
                .disposeBy(composite_disposable)
    }

    private fun checkForLogin()
    {
        ps_auth_dialog_visibility.onNext(SharedPrefsManager.getCurrentUser() == null)
//        ps_auth_dialog_visibility.onNext(true)

        if (SharedPrefsManager.getUserToken() != null)
        {
            Networking().loadUser(
                {
                    bs_user_to_display.onNext(it.asOptional())
                })
        }
    }

    private fun userLogged()
    {
        val user = SharedPrefsManager.getCurrentUser() ?: return
        ps_auth_dialog_visibility.onNext(false)

        if (user.isEmpty())
        {
            val title = getStringMy(R.string.profile)
            val text = getStringMy(R.string.profile_is_empty_fill_now)

            val btn_ok = BtnAction(getStringMy(R.string.fill),
                {
                    val builder = BuilderIntent()
                            .setActivityToStart(ActProfileEdit::class.java)

                    ps_intent_builded.onNext(builder)
                })

            val btn_cancel = BtnAction(getStringMy(R.string.later), null)

            val builder = BuilderDialogMy()
                    .setText(text)
                    .setTitle(title)
                    .setViewId(R.layout.la_dialog_simple)
                    .setBtnOk(btn_ok)
                    .setBtnCancel(btn_cancel)

            ps_to_show_dialog.onNext(builder)
        }
    }


    inner class ViewListener : TabProfileListener
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

            Networking().makeAuth(phone, fb_token,
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

            Networking().makeCodeConfirm(phone!!, code!!,
                {

                    SharedPrefsManager.saveUser(it)
                    bus_main_events.ps_user_logged.onNext(Any())
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

        override fun clickedEditUser()
        {
            val builder = BuilderIntent()
                    .setActivityToStart(ActProfileEdit::class.java)

            ps_intent_builded.onNext(builder)
        }
    }


    inner class Networking
    {
        fun makeAuth(phone: String, fb_token: String?, action_success: () -> Unit)
        {
            api_auth.makeAuth(phone, fb_token)
                    .mainThreaded()
                    .addMyParser<BaseResponse>(BaseResponse::class.java)
                    .addProgress(this@VmTabProfile)
                    .addScreenDisabling(this@VmTabProfile)
                    .addErrorCatcher(this@VmTabProfile)
                    .subscribeMy(
                        {
                            action_success()
                        })
                    .disposeBy(composite_disposable)
        }

        fun makeCodeConfirm(phone: String, code: String, action_success: (ModelUser) -> Unit)
        {
            api_auth.confirmPhone(phone, code)
                    .mainThreaded()
                    .addMyParser<RespUserSingle>(RespUserSingle::class.java)
                    .addProgress(this@VmTabProfile)
                    .addScreenDisabling(this@VmTabProfile)
                    .addErrorCatcher(this@VmTabProfile)
                    .addParseChecker(
                        {
                            return@addParseChecker it.user != null
                        })
                    .subscribeMy(
                        {
                            action_success(it.user!!)
                        })
                    .disposeBy(composite_disposable)
        }

        fun loadUser(action_success: (ModelUser) -> Unit)
        {
            api_auth.getUser()
                    .mainThreaded()
                    .addMyParser<RespUserSingle>(RespUserSingle::class.java)
                    .addProgress(this@VmTabProfile)
                    .addScreenDisabling(this@VmTabProfile)
                    .addErrorCatcher(this@VmTabProfile)
                    .addParseChecker(
                        {
                            return@addParseChecker it.user != null
                        })
                    .subscribeMy(
                        {
                            action_success(it.user!!)
                        })
                    .disposeBy(composite_disposable)
        }
    }

}