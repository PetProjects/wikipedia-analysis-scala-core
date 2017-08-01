package epam.idobrovolskiy.wikipedia.trending.time.date

import java.time.{LocalDate, Month}

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */
case class WikiDate
(
  day: Option[Byte],
  month: Option[Month],
  year: Option[Short],
  decade: Option[Short],
  century: Option[Short],
  thousandYearsAgo: Option[Int],
  isCommonEra: Option[Boolean]
) {

  def toClosetYear: Int = if (year.isDefined) year.get else 0

  def toClosestDecade: Int = if (decade.isDefined) decade.get else 0

  def toClosestCentury: Int = if (century.isDefined) century.get else 0

  def toClosestThousandYears: Int = if (thousandYearsAgo.isDefined) thousandYearsAgo.get else 0

  def toConstructedPositiveYear: Int = toClosetYear + toClosestDecade * 10 + toClosestCentury * 100 + toClosestThousandYears * 1000

  def applyEra(positiveYear: Int): Int = if (isCommonEra.isDefined && isCommonEra.get) positiveYear else -positiveYear

  def getClosestYear = applyEra(toConstructedPositiveYear)

  def getClosestMonth: Month = if (month.isDefined) month.get else Month.JANUARY

  def getClosestDay = if (day.isDefined) day.get.toInt else 1

  val toClosestTraditionalDate: LocalDate = LocalDate.of(getClosestYear, getClosestMonth, getClosestDay)

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: WikiDate => other.isInstanceOf[WikiDate] &&
      other.toClosestTraditionalDate == this.toClosestTraditionalDate
    case _ => false
  }

  override val hashCode: Int = toClosestTraditionalDate.##

  override val toString: String = toClosestTraditionalDate.toString //TODO: extend with WikiDate nuances (for debugging)
}

object WikiDate {
  def date(year: Int) =
    WikiDate(None, None, Some(year.toShort), None, None, None, Some(true))

  def date(year: Int, month: Month) =
    WikiDate(None, Some(month), Some(year.toShort), None, None, None, Some(true))

  def date(year: Int, month: Month, day: Int) =
    WikiDate(Some(day.toByte), Some(month), Some(year.toShort), None, None, None, Some(true))

  def AD(year: Int) =
    date(year)

  def AD(year: Int, month: Month) =
    date(year, month)

  def AD(year: Int, month: Month, day: Int) =
    date(year, month, day)

  def BC(year: Int) =
    WikiDate(None, None, Some(year.toShort), None, None, None, Some(false))

  def BC(year: Int, month: Month) =
    WikiDate(None, Some(month), Some(year.toShort), None, None, None, Some(false))

  def BC(year: Int, month: Month, day: Int) =
    WikiDate(Some(day.toByte), Some(month), Some(year.toShort), None, None, None, Some(false))

  def decade(dec: Int) =
    WikiDate(None, None, None, Some(dec.toShort), None, None, Some(true))

  def century(cent: Int): WikiDate =
    century(cent, true)

  def century_BC(cent: Int): WikiDate =
    century(cent, false)

  def century(cent: Int, isCommonEra: Boolean) =
    WikiDate(None, None, None, None, Some(cent.toShort), None, Some(isCommonEra))

  def thousandYears(thYears: Int): WikiDate =
    thousandYears(thYears, true)

  def thousandYears_BC(thYears: Int): WikiDate =
    thousandYears(thYears, false)

  def thousandYears(thYears: Int, isCommonEra: Boolean) =
    WikiDate(None, None, None, None, None, Some(thYears), Some(isCommonEra))
}