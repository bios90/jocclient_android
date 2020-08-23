package com.justordercompany.client.networking

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.local_data.SharedPrefsManager
import com.justordercompany.client.logic.utils.NotificationManager
import javax.inject.Inject

class FbMessagingService:FirebaseMessagingService()
{
    @Inject
    lateinit var notification_manager:NotificationManager

    init
    {
        AppClass.app_component.inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage)
    {
        super.onMessageReceived(message)
        Log.e("FbMessagingService", "onMessageReceived:")
        notification_manager.notify(message)
    }


    override fun onNewToken(token: String)
    {
        super.onNewToken(token)
        Log.e("FbMessagingService", "onNewToken: got new token $token")
        SharedPrefsManager.saveString(SharedPrefsManager.Key.FB_TOKEN,token)
    }
}