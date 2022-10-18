package com.jisoo.alarmcodelab.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.jisoo.alarmcodelab.MainActivity
import com.jisoo.alarmcodelab.R

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * NotificationManager를 호출하는 확장 기능 추가...
 * send & cancel
 * */

@RequiresApi(Build.VERSION_CODES.S)
fun NotificationManager.sendNotification(msg: String, context: Context) {

    /**
     * 알람을 눌렀을 때 메인액티비티로 돌아가기를 원하므로...
     * pendingIntent를 이용하여 특정상황에서 처리되도록 한다.
     * */
    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_MUTABLE
    )

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.egg_notification_channel_id)
    )
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(msg)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        notify(NOTIFICATION_ID, builder.build())

    val eggImage = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.cooked_egg
    )
}

/**
 * 새 타이머를 시작하는 경우
 * 기존 타이머가 있으면 기존 타이머를 지워준다.
 * */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}