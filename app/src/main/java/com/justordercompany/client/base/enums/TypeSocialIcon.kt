package com.justordercompany.client.base.enums

import com.justordercompany.client.R
import com.justordercompany.client.extensions.getStringMy

enum class TypeSocialIcon
{
    INSTAGRAM,
    VK,
    FACEBOOK,
    WHATSAPP,
    TWITTER;

//    fun getTvViewId():Int
//    {
//
//    }

    fun getFawIcon(): String
    {
        return when (this)
        {
            INSTAGRAM -> getStringMy(R.string.faw_instagram)
            VK -> getStringMy(R.string.faw_vk)
            FACEBOOK -> getStringMy(R.string.faw_facebook)
            WHATSAPP -> getStringMy(R.string.faw_whatsapp)
            TWITTER -> getStringMy(R.string.faw_twitter)
        }
    }
}
