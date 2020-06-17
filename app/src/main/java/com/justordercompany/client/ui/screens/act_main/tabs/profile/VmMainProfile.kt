package com.justordercompany.client.ui.screens.act_main.tabs.profile

import com.justordercompany.client.base.BaseViewModel
import com.justordercompany.client.base.enums.TypeActivityAnim
import com.justordercompany.client.logic.utils.builders.BuilderIntent
import com.justordercompany.client.ui.screens.act_auth.ActAuth

class VmMainProfile : BaseViewModel(), LaMainProfileListener
{
    override fun activityAttached()
    {

    }

    override fun clickedMakeAuth()
    {
        val builder_intent = BuilderIntent()
                .setActivityToStart(ActAuth::class.java)
                .addActivityAnim(TypeActivityAnim.FADE)

        ps_intent_builded.onNext(builder_intent)
    }

}