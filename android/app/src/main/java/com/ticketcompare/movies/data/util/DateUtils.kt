package com.ticketcompare.movies.data.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    /**
     * Dynamically generates the current or future date string (YYYY-MM-DD)
     * using the device/server current time. NEVER hardcoded!
     */
    fun getDynamicDateStr(offsetDays: Int = 0): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(cal.time)
    }

    fun getDayDisplayLabel(offsetDays: Int): String {
        if (offsetDays == 0) return "Today"
        if (offsetDays == 1) return "Tomorrow"
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        return sdf.format(cal.time)
    }
}
