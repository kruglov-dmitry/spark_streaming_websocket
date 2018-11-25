package example.utils

import java.util.Calendar
import java.text.SimpleDateFormat

object TimeUtils {

  private val calendar = Calendar.getInstance
  private val FORMAT_STRING = "yyyy-MM-dd HH:mm:ss"

  def parseEpochMsFromString(stringToParse: String, formatString: String = FORMAT_STRING): Long = {
    val  df = new SimpleDateFormat(FORMAT_STRING)
    df.parse(stringToParse).getTime
  }

  def extractDayOfYear(timeEpochMs: Long): Int = {
    calendar.setTimeInMillis(timeEpochMs)
    calendar.get(Calendar.DAY_OF_YEAR)
  }

  def extractWeekOfYear(timeEpochMs: Long): Int = {
    calendar.setTimeInMillis(timeEpochMs)
    calendar.get(Calendar.WEEK_OF_YEAR)
  }

  def extractMonthOfYear(timeEpochMs: Long): Int = {
    calendar.setTimeInMillis(timeEpochMs)
    calendar.get(Calendar.MONTH)
  }

}
