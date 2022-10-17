package com.jisoo.alarmcodelab.ui

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.jisoo.alarmcodelab.R
import com.jisoo.alarmcodelab.util.sendNotification

class EggTimerViewModel(private val app: Application): AndroidViewModel(app) {

    val notificationManager = ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager

    init {
        notificationManager.sendNotification(app.getString(R.string.timer_running),app)
    }

}