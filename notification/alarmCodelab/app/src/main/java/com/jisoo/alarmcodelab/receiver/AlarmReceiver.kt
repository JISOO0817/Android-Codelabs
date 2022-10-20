package com.jisoo.alarmcodelab.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.jisoo.alarmcodelab.R
import com.jisoo.alarmcodelab.util.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    /**
     * 시간이 다 되면 onReceiver() 트리거
     * sendNotification 호출...
     * */
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.eggs_ready).toString(),
            context
        )
    }
}