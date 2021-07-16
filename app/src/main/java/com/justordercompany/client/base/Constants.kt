package com.justordercompany.client.base

class Constants
{
    companion object
    {
        const val LIST_CAFE_LOAD_DISTACNE = 10
        const val SHARED_PREFS = "Shared_Prefs"
        const val EXTENSION_PNG = "png"
        const val EXTENSION_JPEG = "jpeg"

        const val FOLDER_APP_ROOT = "joc_client"
        const val FOLDER_TEMP_FILES = FOLDER_APP_ROOT + "/temp_files"

        const val COUNT_ADD_ON_LOAD = 10
    }


    object Urls
    {
        const val test_mode = false
        const val API_VERSION = "v1"
        val BEARER_TOKEN = "B1ZpaLlhBgMQ9onHc05oCG8GNbYyIvtd"
        val URL_BASE: String
            get()
            {
                if (test_mode)
                {
                    return "https://dev.justordercompany.com/"
                }
                else
                {
                    return "https://justordercompany.com/"
                }
            }

        const val URL_REGISTER = "api/${API_VERSION}/client/register"
        const val URL_PHONE_CONFIRM = "api/${API_VERSION}/client/confirm"
        const val URL_USER_UPDATE = "api/${API_VERSION}/client/update"
        const val URL_USER_INFO = "api/${API_VERSION}/client/info"

        const val URL_GET_CAFES = "api/${API_VERSION}/cafe/list"
        const val URL_GET_CAFE_SINGLE = "api/${API_VERSION}/cafe/info/{id}"

        const val URL_ORDER_CREATE = "api/${API_VERSION}/order/create"
        const val URL_ORDER_PAY = "api/${API_VERSION}/order/payment/{id}"
        const val URL_ORDER_CANCEL = "api/${API_VERSION}/order/status-update/{id}"
        const val URL_ORDER_GET_INFO = "api/${API_VERSION}/order/info/{id}"
        const val URL_ORDER_GET_USER_ORDERS = "api/${API_VERSION}/order/list"
        const val URL_ORDER_MAKE_REVIEW = "api/${API_VERSION}/cafe/review/{id}"

        const val URL_FAVORITE_GET_ALL = "api/${API_VERSION}/client/favorite"
        const val URL_FAVORITE_ADD = "api/${API_VERSION}/client/favorite/add"
        const val URL_FAVORITE_REMOVE = "api/${API_VERSION}/client/favorite/remove"
    }

    object Extras
    {
        const val EXTRA_FILTER = "Extra_Filter"
        const val EXTRA_CAFE_ID = "Extra_Cafe_Id"
        const val EXTRA_CAFE_NAME = "Extra_Cafe_Name"
        const val EXTRA_CLICKED_VISIT = "Extra_Clicked_Visit"
        const val EXTRA_ROUTE_INFO = "Extra_Route_Info"
        const val EXTRA_PRODUCT = "Extra_Product"
        const val EXTRA_CAN_ORDER = "Extra_Can_Order"
        const val EXTRA_BASKET_ITEM = "Extra_Basket_Item"
        const val EXTRA_PAY_WITH_CARD = "Extra_Pay_With_Card"
        const val EXTRA_PAY_WITH_GOOGLE_PAY = "Extra_Pay_Google_Pay"
        const val EXTRA_ORDER_ID = "Extra_Order_Id"
        const val EXTRA_REVIEW_MADE = "Extra_Review_Made"
    }

    object Payments
    {
        const val SHOP_ID = "724027"

        //        const val SHOP_ID = "721268"
        //Todo donot forget to change
        const val API_KEY = "test_NzI0MDI3O0fCxNm7K8L46XswOyF6e661TFvPCmMGclk"
//        const val API_KEY = "live_NzIxMjY4I2UiYQSiF5Q1IN5dNntIb4ORicC8MElopsU"
    }
}