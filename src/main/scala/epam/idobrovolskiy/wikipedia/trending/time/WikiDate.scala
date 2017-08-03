package epam.idobrovolskiy.wikipedia.trending.time.date

import java.io.{IOException, InvalidObjectException, ObjectInputStream, ObjectOutputStream}
import java.time.{LocalDate, Month}

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */

//TODO: IMPORTANT! Still Ser-De not works well when it comes to java.io.Serializable way (not companion object way of serialize/deserialize). Must be investigated and fixed.

@SerialVersionUID(1L)
//class WikiDate
//(
//  @transient
//  val day: Option[Byte],
//  @transient
//  val month: Option[Month],
//  @transient
//  val year: Option[Short],
//  @transient
//  val decade: Option[Short],
//  @transient
//  val century: Option[Short],
//  @transient
//  val thousandYearsAgo: Option[Int],
//  @transient
//  val isCommonEra: Option[Boolean]
//)
case class WikiDate
(
  @transient
  day: Option[Byte],
  @transient
  month: Option[Month],
  @transient
  year: Option[Int],
  @transient
  decade: Option[Short],
  @transient
  century: Option[Short],
  @transient
  thousandYearsAgo: Option[Int],
  @transient
  isCommonEra: Option[Boolean]
)
  extends AnyRef with Serializable
{

  def toClosetYear: Int = if (year.isDefined) year.get else 0

  def toClosestDecade: Int = if (decade.isDefined) decade.get else 0

  def toClosestCentury: Int = if (century.isDefined) century.get else 0

  def toClosestThousandYears: Int = if (thousandYearsAgo.isDefined) thousandYearsAgo.get else 0

  def toConstructedPositiveYear: Int = toClosetYear + toClosestDecade * 10 + toClosestCentury * 100 + toClosestThousandYears * 1000

  def applyEra(positiveYear: Int): Int = if (isCommonEra.isDefined && isCommonEra.get) positiveYear else -positiveYear

  def getClosestYear = applyEra(toConstructedPositiveYear)

  def getClosestMonth: Month = if (month.isDefined) month.get else Month.JANUARY

  def getClosestDay = if (day.isDefined) day.get.toInt else 1

//  @transient
  val toClosestTraditionalDate: LocalDate = LocalDate.of(getClosestYear, getClosestMonth, getClosestDay)

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: WikiDate => other.isInstanceOf[WikiDate] &&
      other.toClosestTraditionalDate == this.toClosestTraditionalDate
    case _ => false
  }

  @transient
  override val hashCode: Int = toClosestTraditionalDate.##

  @transient
  override val toString: String = toClosestTraditionalDate.toString //TODO: extend with WikiDate nuances (for debugging)

//  @throws[IOException]
//  private def writeObject(stream: ObjectOutputStream) = {
//    stream.writeLong(WikiDate.serialize(this))
//  }
//
//  @throws[IOException]
//  @throws[ClassNotFoundException]
//  private def readObject(stream: ObjectInputStream) =
//    throw new InvalidObjectException("Deserialization via serialization delegate") //Just relaying LocalDate behaviour, which makes sense for immutable WikiDate as well

  def serialize : Long =
    WikiDate.serialize(this)
}

object WikiDate {
//  def apply(
//    day: Option[Byte],
//    month: Option[Month],
//    year: Option[Short],
//    decade: Option[Short],
//    century: Option[Short],
//    thousandYearsAgo: Option[Int],
//    isCommonEra: Option[Boolean]
//  ) = new WikiDate(day, month, year, decade, century, thousandYearsAgo, isCommonEra)

  def date(year: Int) =
    WikiDate(None, None, Some(year), None, None, None, Some(true))

  def date(year: Int, month: Month) =
    WikiDate(None, Some(month), Some(year), None, None, None, Some(true))

  def date(year: Int, month: Month, day: Int) =
    WikiDate(Some(day.toByte), Some(month), Some(year), None, None, None, Some(true))

  def AD(year: Int) =
    date(year)

  def AD(year: Int, month: Month) =
    date(year, month)

  def AD(year: Int, month: Month, day: Int) =
    date(year, month, day)

  def BC(year: Int) =
    WikiDate(None, None, Some(year), None, None, None, Some(false))

  def BC(year: Int, month: Month) =
    WikiDate(None, Some(month), Some(year), None, None, None, Some(false))

  def BC(year: Int, month: Month, day: Int) =
    WikiDate(Some(day.toByte), Some(month), Some(year), None, None, None, Some(false))

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

  def fromTraditionalDate(ld: LocalDate) =
    date(ld.getYear, ld.getMonth, ld.getDayOfMonth)

  def serialize(wikiDate: WikiDate) : Long =
    wikiDate.toClosestTraditionalDate.toEpochDay

  def deserialize(compactDate: Long) : WikiDate =
    fromTraditionalDate(LocalDate.ofEpochDay(compactDate))

  val Now = WikiDate.fromTraditionalDate(LocalDate.now())
  val MinDate = WikiDate.fromTraditionalDate(LocalDate.MIN)
  val MaxDate = WikiDate.fromTraditionalDate(LocalDate.MAX)
}