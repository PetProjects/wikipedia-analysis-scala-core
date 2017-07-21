package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */

class BasicBodyStats(bodyLines: Seq[String]) {
  val BodyLinesCount = bodyLines.length

  override def toString = s"Basic stats=[line count: $BodyLinesCount]"

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: BasicBodyStats =>
      that.isInstanceOf[BasicBodyStats] && that.BodyLinesCount == BodyLinesCount
  }

  override def hashCode = BodyLinesCount.##
}
