package com.ocsen.onestep.Utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        private const val DATE_FORMAT_ISO_8601 = "yyyy-MM-dd HH:mm:ss" // 2018-04-23T18:25:43Z
        val calendar: Calendar = Calendar.getInstance().apply {
            this.setTime(Date())
        }

        fun getDateString(time: Long) = SimpleDateFormat(DATE_FORMAT_ISO_8601).format(time).toString()

    }


}