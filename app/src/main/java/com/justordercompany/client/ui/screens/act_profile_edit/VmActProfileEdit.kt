package com.justordercompany.client.ui.screens.act_profile_edit

import android.util.Log
import com.justordercompany.client.R
import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.asOptional
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.models.ObjWithImageUrl
import com.justordercompany.client.logic.utils.BuilderPermRequest
import com.justordercompany.client.logic.utils.MessagesManager
import com.justordercompany.client.logic.utils.PermissionManager
import io.reactivex.subjects.BehaviorSubject

class VmActProfileEdit : BaseViewModel()
{
    var bs_image: BehaviorSubject<Optional<ObjWithImageUrl>> = BehaviorSubject.create()

    inner class ViewListener() : ActProfileEditListener
    {
        override fun clickedImage()
        {
            checkAndPickImage(
                {
//                    bs_image.onNext(it.asOptional())
                })
        }
    }
}