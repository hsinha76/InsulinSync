package com.hsdroid.insulinsync.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Helper {
    companion object {
        fun formatDosageTime(dosageTime: String): String {
            val sdf24 = SimpleDateFormat("HH:mm", Locale.US)
            val sdf12 = SimpleDateFormat("hh:mm a", Locale.US)

            try {
                val time = sdf24.parse(dosageTime)
                val calendar = Calendar.getInstance()
                calendar.time = time

                return sdf12.format(time)
            } catch (e: Exception) {
                return "Invalid time format"
            }
        }

        fun setAlarm(context: Context, time: Long) {
            val modifyDataIntent = Intent(context, ModifyDataReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, modifyDataIntent, PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (System.currentTimeMillis() < time) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }

        }

    }
}