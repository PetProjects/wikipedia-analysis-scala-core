package epam.idobrovolskiy.wikipedia.trending.cli

import java.time.Month

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 04.08.2017.
  */
object WikiDateParser {

  /*
  {HDF}={historical date format} Examples:
    1971 - treated as 1971 year
    331BC - treated as 331 year BC
    1431/03 - treated as March of 1431 year
    1941/06/22 or 1941.06.22  - treated as June 22, 1941
    NOW - treated as current date (year, month and day).
        Could be skipped in time range, for UNTIL position.
    GENESIS - earliest time, i.e. the oldest time mentioned in Wikipedia.
        Could be skipped in time range, for SINCE position.

  {HDF}-{HDF} among simple case of specifying valid ranges of
        proper SINCE followed by UNTIL {HDF} values, could be:
    1730s - for decade 1730-1739
    {X}th[BC] - for {X}th century, e.g. 5th for 500-599 or 3thBC for [399 BC..300 BC]
    {X}Kya - for {X} thousand years ago, e.g. 5Kya for ~[XXX BC .. XXI BC]
    AD - treated as period [1 AD..NOW]
    BC - treated as period [{}..1 BC]
  */

  private val Aliases = Map("now" -> WikiDate.Now, "genesis" -> WikiDate.MinDate)

  private val YearMonthDayRe = """(\d{1,4})(?:[/\.]([01]\d)(?:[/\.](\d\d))?)?""".r

  private def parseYearMonthDayEra(lcDate: String): Option[WikiDate] = {
    val dateSB = new StringBuilder(lcDate)
    var isAd = true
    if (dateSB.endsWith("ad")) {
      dateSB.delete(dateSB.length - 2, dateSB.length)
    }
    else if (dateSB.endsWith("bc")) {
      dateSB.delete(dateSB.length - 2, dateSB.length)
      isAd = false
    }

    if (dateSB.length == 0) {
      Some(if (isAd) WikiDate.AD else WikiDate.BC)
    }
    else
      dateSB match {
        case YearMonthDayRe(year, month, day) =>
          Some(
            WikiDate(
              if (day == null) None else Some(day.toByte),
              if (month == null) None else Some(Month.of(month.toInt)),
              Some(year.toInt),
              None,
              None,
              None,
              Some(isAd)
            )
          )

        case _ => None
      }
  }

  private def parseDecade(lcDate: String): Option[WikiDate] = ???

  private def parseCentury(lcDate: String): Option[WikiDate] = ???

  private def parseThousandYearsAgo(lcDate: String): Option[WikiDate] = ???

  def parse(date: String): Option[WikiDate] = {
    val lcDate = date.toLowerCase

    Aliases.get(lcDate)
      .orElse(parseYearMonthDayEra(lcDate))
      .orElse(parseDecade(lcDate))
      .orElse(parseCentury(lcDate))
      .orElse(parseThousandYearsAgo(lcDate))
  }
}
