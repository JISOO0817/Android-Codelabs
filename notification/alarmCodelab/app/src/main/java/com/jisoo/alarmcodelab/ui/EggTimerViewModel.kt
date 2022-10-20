package com.jisoo.alarmcodelab.ui

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jisoo.alarmcodelab.R
import com.jisoo.alarmcodelab.receiver.AlarmReceiver
import com.jisoo.alarmcodelab.util.cancelNotifications
import com.jisoo.alarmcodelab.util.sendNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EggTimerViewModel(private val app: Application): AndroidViewModel(app) {

    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

    private val prefs = app.getSharedPreferences("com.jisoo.alarmcodelab",Context.MODE_PRIVATE)

    private val timerLengthOptions: IntArray
    private val notifyPendingIntent: PendingIntent
    private val notifyIntent = Intent(app, AlarmReceiver::class.java)

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long> = _elapsedTime

    private val _timeSelection = MutableLiveData<Int>()
    val timeSelection: LiveData<Int> = _timeSelection

    private val _alarmOn = MutableLiveData<Boolean>()
    val isAlarmOn: LiveData<Boolean> = _alarmOn

    private lateinit var timer: CountDownTimer

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {

        _alarmOn.value = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        ) != null

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        timerLengthOptions = app.resources.getIntArray(R.array.minutes)

        /**
         * 알람이 null이 아니면 알람 타이머 다시 시작
         * */
        if(_alarmOn.value!!) {
            createTimer()
        }

    }

    fun setTimeSelected(timerLengthSelection: Int) {
        _timeSelection.value = timerLengthSelection
    }

    fun setAlarm(isChecked: Boolean) {
        when(isChecked) {
            true -> timeSelection.value?.let {
                startTimer(it)
            }
            false -> cancelNotification()
        }
    }

    private fun startTimer(timerLengthSelection: Int) {
        _alarmOn.value?.let {
            if(!it) {
                _alarmOn.value = true
                val selectedInterval = when (timerLengthSelection) {
                    0 -> second * 10
                    else -> timerLengthOptions[timerLengthSelection] * minute
                }

                val triggerTime = SystemClock.elapsedRealtime() + selectedInterval

                /**
                 * 이전 알람 삭제
                 * */
                val notificationManager =
                    ContextCompat.getSystemService(
                        app,
                        NotificationManager::class.java
                    ) as NotificationManager
                notificationManager.cancelNotifications()

                /**
                 * 도즈모드에서도 알람을 울리게 해줌 (setExactAndAllowWhileIdle)
                 * */
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent
                )

                viewModelScope.launch {
                    saveTime(triggerTime)
                }
            }
        }

        createTimer()
    }

    private fun createTimer() {
        viewModelScope.launch {
            val triggerTime = loadTime()
            timer = object: CountDownTimer(triggerTime, second) {
                override fun onTick(p0: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if(_elapsedTime.value!! <= 0) {
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }

    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false
    }


    private suspend fun saveTime(time: Long) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(TRIGGER_TIME,time).apply()
        }


    private suspend fun loadTime() =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME,0)
        }


    private fun cancelNotification() {
        resetTimer()
        alarmManager.cancel(notifyPendingIntent)
//        _elapsedTime.value = 0
//        _alarmOn.value = false
    }

}