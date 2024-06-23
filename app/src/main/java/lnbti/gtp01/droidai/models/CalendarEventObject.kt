package lnbti.gtp01.droidai.models

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalendarEventObject(val dateString: String, val eventString: String) {
    fun getMonthName(): String {
        try {
            // Parse the date string
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date: Date = sdf.parse(dateString) ?: return ""

            // Convert the date to month format
            val monthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            return monthFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getFormattedDateString(): String {
        return dateString.substring(dateString.lastIndexOf("-") + 1)
    }
}
