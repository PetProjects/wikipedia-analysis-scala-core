package epam.idobrovolskiy.wikipedia.trending.cli

import java.time.{LocalDate, Month}

import epam.idobrovolskiy.wikipedia.trending.time.{WikiDate, WikiDateRange}

/**
  * Created by Igor_Dobrovolskiy on 04.08.2017.
  */
object WikiDateRangeParser {
  val DateDelims = Array('-', '\u0096')

  private def parseSingleDateInRange(delimIndex: Int, date: String): Option[WikiDateRange] =
    WikiDateParser.parse(date) match {
      case Some(date) =>
        if (delimIndex == 0)
          Some(WikiDateRange(WikiDate.MinDate, date))
        else
          Some(WikiDateRange(date, WikiDate.Now))
      case _ => None
    }

  private def parseTwoDates(dateSince: String, dateUntil: String): Option[WikiDateRange] =
    (WikiDateParser.parse(dateSince), WikiDateParser.parse(dateUntil)) match {
      case (Some(dateSince), Some(dateUntil)) =>
        Some(WikiDateRange(dateSince, dateUntil))
      case _ => None
    }

  private def nextMonth(month: Month): Month =
    LocalDate.of(2017, month, 1).plusMonths(1).getMonth

  private def nextYear(year: Int, isAD: Boolean): Int =
    year + (if (isAD) 1 else -1)

  private def inferUntilDateFromGaps(date: WikiDate): WikiDate = date match {
    case WikiDate(None, None, None, None, None, None, Some(true)) =>
      WikiDate.Now //AD-

    case WikiDate(None, None, None, None, None, None, Some(false)) =>
      WikiDate.MinDate //-BC

    case WikiDate(None, None, Some(year), None, None, None, Some(isAD)) =>
      WikiDate(None, None, Some(nextYear(year, isAD)), None, None, None, Some(isAD))

    case WikiDate(None, Some(month), Some(year), None, None, None, Some(isAD)) => {
      val nm = nextMonth(month)
      val uyear = if (nm == Month.JANUARY) nextYear(year, isAD) else year
      WikiDate(None, Some(nm), Some(uyear), None, None, None, Some(isAD))
    }

    case WikiDate(Some(day), Some(month), Some(year), None, None, None, Some(isAD)) => {
      val date = LocalDate.of(year, month, day).plusDays(1)
      WikiDate(Some(date.getDayOfMonth.toByte), Some(date.getMonth), Some(date.getYear), None, None, None, Some(date.getYear > 0))
    }

    //let's crash in the rest of not supported cases at the moment...
    //    case WikiDate(_, _, _, _, _, _, _) => date
  }

  private def swapIfWrongOrder(date1: WikiDate, date2: WikiDate): (WikiDate, WikiDate) =
    if (date1.serialize > date2.serialize)
      (date2, date1) //swap
    else
      (date1, date2)


  private def parseSingleDate(dates: String): Option[WikiDateRange] =
    WikiDateParser.parse(dates) match {
      case Some(date) => {
        val (fromDate, untilDate) = swapIfWrongOrder(date, inferUntilDateFromGaps(date))

        Some(WikiDateRange(fromDate, untilDate))
      }

      case _ => None
    }

  def parse(dates: String): Option[WikiDateRange] = {
    val delimIndex = DateDelims.indexWhere(dates.contains(_))
    if (delimIndex >= 0)
      dates.split(DateDelims).toList match {
        case date :: Nil => parseSingleDateInRange(delimIndex, date)
        case dateSince :: dateUntil :: Nil => parseTwoDates(dateSince, dateUntil)
        case _ => None
      }
    else
      parseSingleDate(dates)
  }
}