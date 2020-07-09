package com.justordercompany.client.ui.screens.act_profile_edit

import android.util.Log
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.*
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.models.ModelUser
import com.justordercompany.client.logic.models.ObjWithImageUrl
import com.justordercompany.client.logic.responses.RespUserSingle
import com.justordercompany.client.logic.utils.builders.BuilderCropMy
import com.justordercompany.client.logic.utils.files.MyFileItem
import com.justordercompany.client.logic.utils.files.toBase64
import com.justordercompany.client.networking.apis.ApiAuth
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MultipartBody
import javax.inject.Inject

class VmActProfileEdit : BaseViewModel()
{
    @Inject
    lateinit var api_auth: ApiAuth

    var bs_image: BehaviorSubject<Optional<ObjWithImageUrl>> = BehaviorSubject.create()
    var bs_name: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    var bs_email: BehaviorSubject<Optional<String>> = BehaviorSubject.createDefault(String.getNullString().asOptional())
    val bs_user_to_display: BehaviorSubject<Optional<ModelUser>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)

        Networking().loadUser(
            {
                bs_user_to_display.onNext(it.asOptional())
            })
    }


    inner class ViewListener() : ActProfileEditListener
    {
        override fun clickedImage()
        {
            checkAndPickImage(
                { my_file_original ->

                    val builder = BuilderCropMy()
                            .setFileOriginal(my_file_original)
                            .setActionSuccess(
                                { my_file_croppped ->
                                    bs_image.onNext(my_file_croppped.asOptional())
                                })
                            .setActionError(
                                {
                                    bs_image.onNext(my_file_original.asOptional())
                                })

                    ps_ucrop_action.onNext(builder)
                })
        }

        override fun clickedSave()
        {
            val name = bs_name.value?.value
            val email = bs_email.value?.value
            val push_token = SharedPrefsManager.getString(SharedPrefsManager.Key.FB_TOKEN)
            var image_base64: String? = null

            bs_image.value?.value.let(
                {
                    if (it is MyFileItem)
                    {
                        image_base64 = it.getFile().toBase64(true)
                    }
                })

            Networking().updateUser(name, email, push_token, image_base64,
                {
                    bus_main_events.ps_user_profile_updated.onNext(Any())
                    ps_to_finish.onNext(Optional(null))
                })
        }
    }


    inner class Networking()
    {
        fun updateUser(name: String?, email: String?, push_token: String?, image_str: String?, action_success: (ModelUser) -> Unit)
        {
            api_auth.updateUserInfo(name, email, push_token, image_str)
                    .mainThreaded()
                    .addMyParser<RespUserSingle>(RespUserSingle::class.java)
                    .addProgress(this@VmActProfileEdit)
                    .addScreenDisabling(this@VmActProfileEdit)
                    .addErrorCatcher(this@VmActProfileEdit)
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
                    .addProgress(this@VmActProfileEdit)
                    .addScreenDisabling(this@VmActProfileEdit)
                    .addErrorCatcher(this@VmActProfileEdit)
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