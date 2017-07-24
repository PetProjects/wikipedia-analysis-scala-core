package epam.idobrovolskiy.wikipedia.analysis

import epam.idobrovolskiy.wikipedia.analysis.tokenizer.StopWordsTokenizer

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */

class BasicBodyStats(bodyLines: Seq[String]) {
  val BodyLinesCount = bodyLines.length
  val N = 10

  lazy val TopNTokens = tokenizer.tokenize(bodyLines).toList.sortBy(-_._2).take(N).toMap

  private lazy val topNTokensString = TopNTokens.toList
    .map { //TODO: remove after finalized stop words
      case (k, v) if (k.length == 1) => (k + f"<${k(0).toShort}%04X>", v)
      case other => other
    }
    .map {case (k, v) => s"$k -> $v"}
    .mkString(", ")

  override def toString = s"Basic stats={line count: $BodyLinesCount; top words=[$topNTokensString]}"

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: BasicBodyStats =>
      that.isInstanceOf[BasicBodyStats] &&
        that.BodyLinesCount == BodyLinesCount &&
        that.TopNTokens == TopNTokens
  }

  override def hashCode = (BodyLinesCount, TopNTokens).##

  private val tokenizer = new StopWordsTokenizer
}
