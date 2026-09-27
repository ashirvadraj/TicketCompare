package com.ticketcompare.movies.data.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    val IST_ZONE_ID: ZoneId = ZoneId.of("Asia/Kolkata")
    private val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
    private val DISPLAY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.US)

    /**
     * Dynamically generates the current or future date string (YYYY-MM-DD)
     * strictly computed in the Asia/Kolkata Indian Standard Timezone.
     */
    fun getDynamicDateStr(offsetDays: Int = 0): String {
        val nowIst = ZonedDateTime.now(IST_ZONE_ID).plusDays(offsetDays.toLong())
        return nowIst.format(ISO_DATE_FORMATTER)
    }

    fun getDayDisplayLabel(offsetDays: Int): String {
        if (offsetDays == 0) return "Today"
        if (offsetDays == 1) return "Tomorrow"
        val nowIst = ZonedDateTime.now(IST_ZONE_ID).plusDays(offsetDays.toLong())
        return nowIst.format(DISPLAY_DATE_FORMATTER)
    }

    fun getTodayDateStr(): String = getDynamicDateStr(0)

    /**
     * Requirement 9: Strict Date/Time Validation in Asia/Kolkata
     * - showDate > today: ALWAYS ALLOWED (Future date shows must never be filtered by current clock time!)
     * - showDate == today: Only future shows (startTimestamp > nowIstMs) are allowed.
     * - showDate < today: Strictly omitted.
     */
    fun isShowTimeValid(showDate: String, startTimestamp: Long?): Boolean {
        val todayStr = getTodayDateStr()
        val nowIstMs = ZonedDateTime.now(IST_ZONE_ID).toInstant().toEpochMilli()

        return when {
            showDate > todayStr -> true // Future date: valid regardless of time!
            showDate == todayStr -> {
                // Today: show must not have already started
                startTimestamp == null || startTimestamp > nowIstMs
            }
            else -> false // Past date
        }
    }

    /**
     * Formats verification age into human readable text:
     * e.g. "32s ago", "1m ago"
     */
    fun formatVerificationAge(ageSeconds: Long): String {
        return when {
            ageSeconds <= 5 -> "just now"
            ageSeconds < 60 -> "${ageSeconds}s ago"
            ageSeconds < 3600 -> "${ageSeconds / 60}m ago"
            else -> "${ageSeconds / 3600}h ago"
        }
    }
}
