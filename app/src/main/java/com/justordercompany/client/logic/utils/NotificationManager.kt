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
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.ui.screens.DummyPendingIntentActivity

class NotificationManager
{
    companion object
    {
        fun notify(remoteMessage: RemoteMessage)
        {
            val notification = getNotification()

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

        fun getNotification(): Notification
        {
            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val title = "Test name!!!"
            val body = "Test name for display!!"

            val builder = NotificationCompat.Builder(AppClass.app, getStringMy(R.string.notify_chanel_joc))
                    .setSmallIcon(R.drawable.star_filled)
                    .setLargeIcon(BitmapFactory.decodeResource(AppClass.app.getResources(), R.drawable.alerter_ic_face))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(getIntentFromPush())
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

        fun getIntentFromPush(): PendingIntent
        {
            val intent = Intent(AppClass.app, DummyPendingIntentActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(AppClass.app, (0..999).random(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
            return pendingIntent
        }
    }
}
