package com.justordercompany.client.ui.animation

import android.view.animation.AlphaAnimation

class AnimationManager
{
    companion object
    {
        fun getActivityFadeOut(from:Float = 0f,to:Float = 1f):AlphaAnimation
        {
            val anim = AlphaAnimation(from,to)
            return anim
        }

        fun getActivityFadeIn(from:Float = 1f,to:Float = 0f):AlphaAnimation
        {
            val anim = AlphaAnimation(from,to)
            return anim
        }
    }
}