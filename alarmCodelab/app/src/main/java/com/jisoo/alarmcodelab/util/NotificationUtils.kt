package com.jisoo.alarmcodelab.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.jisoo.alarmcodelab.MainActivity
import com.jisoo.alarmcodelab.R

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(msg: String, context: Context) {

    val contentIntent = Intent(context, MainActivity::class.java)

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.egg_notification_channel_id)
    )
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(msg)
        notify(NOTIFICATION_ID, builder.build())
}