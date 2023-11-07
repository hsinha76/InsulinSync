package com.hsdroid.insulinsync.utils

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



    }
}