package com.justordercompany.client.base

class Constants
{
    object Urls
    {
        const val test_mode = true
        const val API_VERSION = "v1"
        val BEARER_TOKEN = "930b1a5d-4a13-4f57-942d-a8135c27bb56"
        val URL_BASE: String
            get()
            {
                if (test_mode)
                {
                    return "https://dev.justordercompany.com/"
                }
                else
                {
                    return "https://dev.justordercompany.com/"
                }
            }

        const val URL_REGISTER = "api/${API_VERSION}/client/register"

    }
}