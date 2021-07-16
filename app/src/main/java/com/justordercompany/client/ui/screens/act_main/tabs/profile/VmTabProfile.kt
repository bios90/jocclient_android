package com.justordercompany.client.ui.screens.act_main.tabs.profile

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.*
import com.justordercompany.client.base.enums.TypeAuthMode
import com.justordercompany.client.base.enums.TypeOrderStatus
import com.justordercompany.client.extensions.*
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelOrder
import com.justordercompany.client.logic.models.ModelUser
import com.justordercompany.client.logic.models.replaceWith
import com.justordercompany.client.logic.requests.FeedLoadInfo
import com.justordercompany.client.logic.responses.BaseResponse
import com.justordercompany.client.logic.responses.RespUserSingle
import com.justordercompany.client.logic.utils.BtnAction
import com.justordercompany.client.logic.utils.ValidationManager
import com.justordercompany.client.logic.utils.builders.BuilderAlerter
import com.justordercompany.client.logic.utils.builders.BuilderDialogBottom
import com.justordercompany.client.logic.utils.builders.BuilderDialogMy
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_favorites.ActFavorites
import com.justordercompany.client.ui.screens.act_info_dialog.ActInfoDialog
import com.justordercompany.client.ui.screens.act_profile_edit.ActProfileEdit
import com.justordercompany.client.ui.screens.act_review_dialog.ActReviewDialog
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class VmTabProfile : BaseViewModel()
{
    val ps_auth_dialog_visibility: PublishSubject<Boolean> = PublishSubject.create()
    val bs_auth_mode: BehaviorSubject<TypeAuthMode> = BehaviorSubject.createDefault(TypeAuthMode.PHONE)
    val bs_phone: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_code: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_offert_checked: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val bs_user_to_display: BehaviorSubject<Optional<ModelUser>> = BehaviorSubject.create()
    val ps_to_load_orders: PublishSubject<FeedLoadInfo<ModelOrder>> = PublishSubject.create()
    val bs_orders: BehaviorSubject<FeedDisplayInfo<ModelOrder>> = BehaviorSubject.create()
    val ps_orders_scrolled_to_bottom: PublishSubject<Any> = PublishSubject.create()

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
                        reloadUserInfo()
                    })
                .disposeBy(composite_disposable)

        bus_main_events.bs_order_made
                .subscribe(
                    {
                        makeOrdersFullReload()
                        reloadUserInfo()

                        val text = "Номер вашего «$it», вас уведомят о его готовности"
                        BuilderDialogMy()
                                .setViewId(R.layout.la_dialog_simple)
                                .setTitle(getStringMy(R.string.order_made))
                                .setText(text)
                                .setBtnOk(BtnAction(getStringMy(R.string.ok), {}))
                                .sendInVm(this)

                    })
                .disposeBy(composite_disposable)

        ps_to_load_orders
                .subscribe(
                    {
                        base_networker.loadOrders(it)
                    })
                .disposeBy(composite_disposable)

        ps_orders_scrolled_to_bottom
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(
                    {
                        val info = bs_orders.value.getNextLoadInfo()

                        info.action_success =
                                {
                                    val display_info = bs_orders.value.addElements(it)
                                    bs_orders.onNext(display_info)
                                }

                        ps_to_load_orders.onNext(info)
                    })
                .disposeBy(composite_disposable)
    }

    private fun checkForLogin()
    {
        ps_auth_dialog_visibility.onNext(SharedPrefsManager.getCurrentUser() == null)

        if (SharedPrefsManager.getUserToken() != null)
        {
            makeOrdersFullReload()
            reloadUserInfo()
        }
    }

    private fun reloadUserInfo()
    {
        base_networker.loadUser(
            {
                bs_user_to_display.onNext(it.asOptional())
            })
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

        makeOrdersFullReload()
        reloadUserInfo()
        ps_to_hide_keyboard.onNext(Any())
    }

    fun makeOrdersFullReload()
    {
        val info = FeedLoadInfo<ModelOrder>(0, Constants.COUNT_ADD_ON_LOAD,
            {
                bs_orders.onNext(FeedDisplayInfo(it, LoadBehavior.FULL_RELOAD))
            })

        ps_to_load_orders.onNext(info)
    }


    inner class ViewListener : TabProfileListener
    {
        override fun clickedOrder(order: ModelOrder)
        {
            val order_id = order.id ?: return
            base_networker.getOrderInfo(order_id,
                {
                    val btn_repeat = BtnAction(getStringMy(R.string.repeat), { clickedOrderRepeat(order) })
                    val btn_cancel = BtnAction(getStringMy(R.string.make_cancel), { clickedOrderCancel(order) })
                    val btn_review = BtnAction(getStringMy(R.string.review), { clickedOrderReview(order) })

                    var builder: BuilderDialogBottom? = null

                    //Todo later set only needed actions
                    if (order.status == TypeOrderStatus.DONE)
                    {
                        builder = BuilderDialogBottom()
                                .addBtn(btn_cancel)
                                .addBtn(btn_repeat)
                                .addBtn(btn_review)
                    }
                    else if (order.canBeCancelled())
                    {
                        builder = BuilderDialogBottom()
                                .addBtn(btn_cancel)
                                .addBtn(btn_repeat)
                                .addBtn(btn_review)
                    }
                    else
                    {
                        builder = BuilderDialogBottom()
                                .addBtn(btn_cancel)
                                .addBtn(btn_repeat)
                                .addBtn(btn_review)
                    }

                    ps_to_show_bottom_dialog.onNext(builder)
                })
        }

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
                })
        }


        override fun clickedOffertRules()
        {
            val btn_ok = BtnAction(getStringMy(R.string.its_clear), null)
            val text = getStringMy(R.string.offert)
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

        override fun swipedToRefresh()
        {
            makeOrdersFullReload()
        }

        override fun scrolledToBottom()
        {
            ps_orders_scrolled_to_bottom.onNext(Any())
        }

        fun clickedOrderRepeat(order: ModelOrder)
        {
            val cafe_id = order.cafe?.id ?: return
            val order_id = order.id ?: return

            val builder = BuilderIntent()
                    .setActivityToStart(ActCafeMenu::class.java)
                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
                    .addParam(Constants.Extras.EXTRA_ORDER_ID, order_id)

            ps_intent_builded.onNext(builder)
        }

        fun clickedOrderCancel(order: ModelOrder)
        {
            val order_id = order.id ?: return
            base_networker.cancelOrder(order_id,
                {
                    base_networker.getOrderInfo(order_id,
                        {
                            val current_items = bs_orders.value?.items?.toCollection(ArrayList())
                            if (current_items != null)
                            {
                                current_items.replaceWith(it)
                                val pos = current_items.indexOf(it)
                                if (pos >= 0)
                                {
                                    bs_orders.onNext(FeedDisplayInfo(current_items, LoadBehavior.getUpdateAtPos(pos)))
                                }
                            }

                        })
                })
        }

        fun clickedOrderReview(order: ModelOrder)
        {
            val order_id = order.id ?: return
            val builder = BuilderIntent()
                    .setActivityToStart(ActReviewDialog::class.java)
                    .addParam(Constants.Extras.EXTRA_ORDER_ID, order_id)
                    .setOkAction(
                        {
                            if (it?.getBooleanExtra(Constants.Extras.EXTRA_REVIEW_MADE, false) == true)
                            {
                                reloadUserInfo()
                            }
                        })
            ps_intent_builded.onNext(builder)
        }

        override fun clickedQuestion()
        {
            BuilderIntent()
                    .setActivityToStart(ActInfoDialog::class.java)
                    .sendInVm(this@VmTabProfile)
//            val user = SharedPrefsManager.getCurrentUser()
//            val title = "Письмо из приложения Joc"
//            var text = ""
//            if(user != null)
//            {
//                text+="Пользователь: ${user.name}"
//                text+="\nТелефон: ${user.phone}"
//            }
//
//            emailIntent("jocforusers@gmail.con",text,title)
        }

        override fun clickedFavorites()
        {
            BuilderIntent()
                    .setActivityToStart(ActFavorites::class.java)
                    .setOkAction(
                        {
                            val cafe_id = it?.getIntExtraMy(Constants.Extras.EXTRA_CAFE_ID) ?: return@setOkAction

                            BuilderIntent()
                                    .setActivityToStart(ActCafeMenu::class.java)
                                    .addParam(Constants.Extras.EXTRA_CAFE_ID, cafe_id)
                                    .sendInVm(this@VmTabProfile)
                        })
                    .sendInVm(this@VmTabProfile)
        }
    }

}