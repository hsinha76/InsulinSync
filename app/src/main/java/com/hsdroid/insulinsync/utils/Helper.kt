package com.hsdroid.insulinsync.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.hsdroid.insulinsync.models.Insulin
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Helper {
    companion object {
        fun formatDosageTime(dosageTime: Long): String {
            val sdf24 = SimpleDateFormat("HH:mm", Locale.US)
            val sdf12 = SimpleDateFormat("hh:mm a", Locale.US)

            try {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dosageTime

                val time = calendar.time

                return sdf12.format(time)
            } catch (e: Exception) {
                return "Invalid time format"
            }
        }

        fun storeTimeinMiliseconds(dosageTime: String): Long {
            if (Build.VERSION.SDK_INT <= 26) {
                val sdf24 = SimpleDateFormat("HH:mm", Locale.US)

                return try {
                    val time = sdf24.parse(dosageTime)
                    val calendar = Calendar.getInstance()

                    // Set the date fields of the calendar to today's date
                    val currentDate = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                    calendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                    calendar.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))

                    // Set the time field of the calendar to the parsed time
                    calendar.set(Calendar.HOUR_OF_DAY, time.hours)
                    calendar.set(Calendar.MINUTE, time.minutes)
                    calendar.set(Calendar.SECOND, 0) // Optional: Set seconds to 0

                    // Calculate the time in milliseconds
                    calendar.timeInMillis
                } catch (e: Exception) {
                    -1L // Return -1 to indicate an error or an invalid time format
                }

            } else {
                val formatter = DateTimeFormatter.ofPattern("[H:]m")
                val parsedTime = LocalTime.parse(dosageTime, formatter)

                val currentDate = LocalDate.now()
                val zoneId = ZoneId.systemDefault()
                val dateTime = LocalDateTime.of(currentDate, parsedTime).atZone(zoneId)
                val millis = dateTime.toInstant().toEpochMilli()

                return millis
            }
        }

        fun setAlarm(context: Context, insulin: Insulin) {
            val time = insulin.dosageTime

            val modifyDataIntent = Intent(context, ModifyDataReceiver::class.java)
            modifyDataIntent.putExtra("listData", insulin)

            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, modifyDataIntent, PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (System.currentTimeMillis() < time) {
                Log.d("harish", "Alarm set")
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }

        }

    }
}