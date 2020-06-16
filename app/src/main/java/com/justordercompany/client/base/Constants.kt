package com.justordercompany.client.base

class Constants
{
    object Urls
    {
        const val test_mode = true
        val api_version = "v1"
        val URL_BASE:String
        get()
        {
            if(test_mode)
            {
                return "https://dev.justordercompany.com"
            }
            else
            {
                return "https://dev.justordercompany.com"
            }
        }


    }
}