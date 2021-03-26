package com.justordercompany.client.logic.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.justordercompany.client.R
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.BusMainEvents
import com.justordercompany.client.base.Constants
import com.justordercompany.client.base.enums.TypeTab
import com.justordercompany.client.extensions.*
import com.justordercompany.client.ui.screens.DummyPendingIntentActivity
import com.justordercompany.client.ui.screens.act_cafe_menu.ActCafeMenu
import com.justordercompany.client.ui.screens.act_main.ActMain
import javax.inject.Inject

class MyPush(val title: String, val text: String, val cafe_id: Int?, val order_id: Int?)
{
    companion object
    {
        fun initFromRemoteMessage(remote: RemoteMessage): MyPush
        {
            val title = remote.getString("title") ?: getStringMy(R.string.app_name)
            val text = remote.getString("text") ?: getStringMy(R.string.notify_chanel_joc)
            val cafe_id = remote.getInt("cafe_id")
            val order_id = remote.getInt("order_id")

            return MyPush(title, text, cafe_id, order_id)
        }
    }
}

class NotificationManager()
{
    @Inject
    lateinit var bus_main_events: BusMainEvents

    init
    {
        AppClass.app_component.inject(this)
    }

    fun notify(remoteMessage: RemoteMessage)
    {
        val my_push = MyPush.initFromRemoteMessage(remoteMessage)
        val notification = getNotification(my_push)

        Log.e("NotificationManager", "notify: Got push ${my_push.toJsonMy()}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            showHighNotification(notification)
        }
        else
        {
            showLowNotification(notification)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun showHighNotification(notification: Notification)
    {
        val notificationChannel = NotificationChannel(getStringMy(R.string.notify_chanel_joc), getStringMy(R.string.notify_chanel_joc), NotificationManager.IMPORTANCE_HIGH)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        notificationChannel.description = getStringMy(R.string.notify_chanel_joc)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.setSound(sound, att)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(false)

        val notificationManager = AppClass.app.getSystemService(android.app.NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)
        notificationManager?.notify((0..999).random(), notification)
    }

    fun getNotification(my_push: MyPush): Notification
    {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(AppClass.app, getStringMy(R.string.notify_chanel_joc))
                .setSmallIcon(R.drawable.star_filled)
                .setLargeIcon(BitmapFactory.decodeResource(AppClass.app.getResources(), R.drawable.alerter_ic_face))
                .setContentTitle(my_push.title)
                .setContentText(my_push.text)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getIntentFromPush(my_push))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(sound)

        return builder.build()
    }

    private fun showLowNotification(notification: Notification)
    {
        val notificationManagerCompat = NotificationManagerCompat.from(AppClass.app)
        notificationManagerCompat.notify((0..999).random(), notification)
    }

    fun getIntentFromPush(my_push: MyPush): PendingIntent
    {
        val intent: Intent
        if (my_push.cafe_id != null)
        {
            intent = Intent(AppClass.app, ActCafeMenu::class.java)
            intent.putExtra(Constants.Extras.EXTRA_CAFE_ID, my_push.cafe_id)
        }
        else if (my_push.order_id != null)
        {
            intent = Intent(AppClass.app, ActMain::class.java)
            intent.makeClearAllPrevious()
            bus_main_events.bs_current_tab.onNext(TypeTab.PROFILE)
        }
        else
        {
            intent = Intent(AppClass.app, DummyPendingIntentActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(AppClass.app, (0..999).random(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}
