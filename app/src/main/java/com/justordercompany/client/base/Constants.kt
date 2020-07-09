package com.justordercompany.client.base

class Constants
{
    companion object
    {
        const val SHARED_PREFS = "Shared_Prefs"
        const val EXTENSION_PNG = "png"
        const val EXTENSION_JPEG = "jpeg"

        const val FOLDER_APP_ROOT = "joc_client"
        const val FOLDER_TEMP_FILES = FOLDER_APP_ROOT + "/temp_files"

    }


    object Urls
    {

        const val test_mode = true
        const val API_VERSION = "v1"
//        val BEARER_TOKEN = "930b1a5d-4a13-4f57-942d-a8135c27bb56"
        val BEARER_TOKEN = "psgeJ94WvoAAVtULBQBOSiUOAytGy4DS"
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
        const val URL_PHONE_CONFIRM = "api/${API_VERSION}/client/confirm"
        const val URL_USER_UPDATE = "api/${API_VERSION}/client/update"
        const val URL_USER_INFO = "api/${API_VERSION}/client/info"

        const val URL_GET_CAFES = "api/${API_VERSION}/cafe"
        const val URL_GET_CAFE_SINGLE = "api/${API_VERSION}/cafe/{id}"
    }

    object Extras
    {
        const val EXTRA_FILTER = "Extra_Filter"
        const val EXTRA_CAFE_ID = "Extra_Filter"
    }
}