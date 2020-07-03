package com.justordercompany.client.networking

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.justordercompany.client.local_data.SharedPrefsManager

class FbMessagingService:FirebaseMessagingService()
{
    override fun onMessageReceived(message: RemoteMessage)
    {
        super.onMessageReceived(message)


    }


    override fun onNewToken(token: String)
    {
        super.onNewToken(token)
        SharedPrefsManager.saveString(SharedPrefsManager.Key.FB_TOKEN,token)
    }
}