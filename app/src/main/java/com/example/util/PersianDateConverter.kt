package com.example.util

/**
 * Robust Gregorian to Persian (Jalali / Solar Hijri) date converter and English date formatter.
 */
object PersianDateConverter {

    /**
     * Formats timestamp based on current selected app language.
     */
    private val monthAbbrMap = mapOf(
        "jan" to 1, "feb" to 2, "mar" to 3, "apr" to 4,
        "may" to 5, "jun" to 6, "jul" to 7, "aug" to 8,
        "sep" to 9, "oct" to 10, "nov" to 11, "dec" to 12
    )

    /**
     * Formats timestamp based on current selected app language.
     * Supports SQL/ISO dates (YYYY-MM-DD HH:MM:SS), RFC 2822 email dates (e.g., Fri, 23 Oct 2026 14:30:00 +0000),
     * and English dates (Oct 23, 2026 - 14:30).
     */
    fun formatDateTime(raw: String?, lang: AppLanguage): String {
        if (raw.isNullOrBlank() || raw.equals("null", ignoreCase = true)) {
            return AppStrings.notUpdated(lang)
        }

        val trimmed = raw.trim()

        var gYear: Int? = null
        var gMonth: Int? = null
        var gDay: Int? = null
        var hour: String? = null
        var minute: String? = null

        // 1. Standard YYYY-MM-DD or YYYY/MM/DD [HH:MM]
        val isoRegex = Regex("""(\d{4})[/-](\d{1,2})[/-](\d{1,2})(?:[ T](\d{1,2}):(\d{1,2}))?""")
        val isoMatch = isoRegex.find(trimmed)
        if (isoMatch != null) {
            gYear = isoMatch.groupValues[1].toIntOrNull()
            gMonth = isoMatch.groupValues[2].toIntOrNull()
            gDay = isoMatch.groupValues[3].toIntOrNull()
            hour = isoMatch.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }
            minute = isoMatch.groupValues.getOrNull(5)?.takeIf { it.isNotBlank() }
        } else {
            // 2. RFC 2822 Email Date: e.g. "Fri, 23 Oct 2026 23:51:12 +0000" or "23 Oct 2026 14:30"
            val rfcRegex = Regex("""\b(\d{1,2})\s+([A-Za-z]{3})[a-z]*\s+(\d{4})(?:\s+(\d{1,2}):(\d{1,2}))?""")
            val rfcMatch = rfcRegex.find(trimmed)
            if (rfcMatch != null) {
                gDay = rfcMatch.groupValues[1].toIntOrNull()
                gMonth = monthAbbrMap[rfcMatch.groupValues[2].lowercase()]
                gYear = rfcMatch.groupValues[3].toIntOrNull()
                hour = rfcMatch.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }
                minute = rfcMatch.groupValues.getOrNull(5)?.takeIf { it.isNotBlank() }
            } else {
                // 3. Month Day, Year: e.g. "Oct 23, 2026 - 14:30"
                val enRegex = Regex("""\b([A-Za-z]{3})[a-z]*\s+(\d{1,2}),?\s+(\d{4})(?:[\s-]+(\d{1,2}):(\d{1,2}))?""")
                val enMatch = enRegex.find(trimmed)
                if (enMatch != null) {
                    gMonth = monthAbbrMap[enMatch.groupValues[1].lowercase()]
                    gDay = enMatch.groupValues[2].toIntOrNull()
                    gYear = enMatch.groupValues[3].toIntOrNull()
                    hour = enMatch.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }
                    minute = enMatch.groupValues.getOrNull(5)?.takeIf { it.isNotBlank() }
                }
            }
        }

        if (gYear == null || gMonth == null || gDay == null) {
            return trimmed
        }

        val timePart = if (hour != null && minute != null) {
            val formattedH = hour.padStart(2, '0')
            val formattedM = minute.padStart(2, '0')
            " - $formattedH:$formattedM"
        } else {
            ""
        }

        return if (lang == AppLanguage.PERSIAN) {
            // If already a Jalali year (e.g., 1400..1450), format directly
            val jalali = if (gYear in 1350..1499) {
                JalaliDate(gYear, gMonth, gDay)
            } else {
                gregorianToJalali(gYear, gMonth, gDay)
            }
            val monthName = getPersianMonthName(jalali.month)
            "${jalali.day} $monthName ${jalali.year}$timePart"
        } else {
            val monthNamesEn = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            val monthName = if (gMonth in 1..12) monthNamesEn[gMonth - 1] else gMonth.toString()
            "$monthName $gDay, $gYear$timePart"
        }
    }

    /**
     * Converts a timestamp string into a Persian date string.
     */
    fun formatToPersianDateTime(raw: String?): String {
        return formatDateTime(raw, AppLanguage.PERSIAN)
    }

    data class JalaliDate(val year: Int, val month: Int, val day: Int)

    fun gregorianToJalali(gYear: Int, gMonth: Int, gDay: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

        var gy = gYear - 1600
        var gm = gMonth - 1
        var gd = gDay - 1

        var gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400

        for (i in 0 until gm) {
            gDayNo += gDaysInMonth[i]
        }
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) {
            gDayNo++
        }
        gDayNo += gd

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var jm = 0
        while (jm < 11 && jDayNo >= jDaysInMonth[jm]) {
            jDayNo -= jDaysInMonth[jm]
            jm++
        }
        val jd = jDayNo + 1

        return JalaliDate(jy, jm + 1, jd)
    }

    private fun getPersianMonthName(month: Int): String {
        return when (month) {
            1 -> "فروردین"
            2 -> "اردیبهشت"
            3 -> "خرداد"
            4 -> "تیر"
            5 -> "مرداد"
            6 -> "شهریور"
            7 -> "مهر"
            8 -> "آبان"
            9 -> "آذر"
            10 -> "دی"
            11 -> "بهمن"
            12 -> "اسفند"
            else -> ""
        }
    }
}
