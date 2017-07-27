package epam.idobrovolskiy.wikipedia.trending.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */

class BasicBodyStats(
  //bodyLines: Seq[String]
  val linesCount: Int,
  val topNTokens: Map[String, Int])
{
  private lazy val topNTokensString = topNTokens.toList
    .map { //TODO: remove after finalized stop words
      case (k, v) if k.length == 1 => (k + f"<${k(0).toShort}%04X>", v)
      case other => other
    }
    .map { case (k, v) => s"$k->$v" }
    .mkString(", ")

  override def toString = s"Basic stats={line count: $linesCount; top words=[$topNTokensString]}"

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: BasicBodyStats =>
      that.isInstanceOf[BasicBodyStats] &&
        that.linesCount == linesCount &&
        that.topNTokens == topNTokens
  }

  override def hashCode: Int = (linesCount, topNTokens).##
}
