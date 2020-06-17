package com.justordercompany.client.base.enums

import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.justordercompany.client.R
import com.justordercompany.client.ui.animation.AnimationManager

enum class TypeActivityAnim
{
    FADE;

    fun animateWithActivity(activity: AppCompatActivity)
    {
        when (this)
        {
            FADE ->
            {
                activity.overridePendingTransition(R.anim.anim_fade_out,R.anim.anim_fade_in)
            }
        }
    }
}