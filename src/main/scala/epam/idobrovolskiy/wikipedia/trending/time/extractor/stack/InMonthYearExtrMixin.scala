package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import java.text.DateFormatSymbols
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 09.08.2017.
  */
trait InMonthYearExtrMixin extends BasicStackedDatesExtractor {

  protected val TextToMonths: Map[String, Month] =
    Month.values.map(m => (m.getDisplayName(TextStyle.FULL, Locale.US).toLowerCase, m)).toMap

  protected val reMonths = TextToMonths.keys.mkString("(", "|", ")")

  private val re = ("""(?:[^\w]|^)in\s""" + reMonths + """\s(\d{3,4})(?:[\s\.,]|$)""").r
  private val yearGroup = 2
  private val monthGroup = 1

  def extractInMonthYearDates(id: Int, s: String): Iterator[WikiDateExtraction] =
    for {cMatch <- (re findAllIn s).matchData} yield
      WikiDateExtraction(
        WikiDate.AD(cMatch.group(yearGroup).toInt, TextToMonths(cMatch.group(monthGroup))),
        cMatch.start(yearGroup),
        cMatch)

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[WikiDateExtraction]) =
    super.appendDates(id, s, it ++ extractInMonthYearDates(id, s))
}