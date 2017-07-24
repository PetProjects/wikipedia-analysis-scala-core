package epam.idobrovolskiy.wikipedia.analysis

import epam.idobrovolskiy.wikipedia.analysis.tokenizer.StopWordsTokenizer

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */

class BasicBodyStats(bodyLines: Seq[String]) {
  val BodyLinesCount = bodyLines.length
  lazy val Top10Tokens = tokenizer.tokenize(bodyLines).toList.sortBy(-_._2).take(10).toMap

  private lazy val top10TokensString = Top10Tokens.toList.map { //TODO: remove after finalized stop words
    case (k, v) if (k.length == 1) => (k + f" <${k(0).toShort}%08X>", v)
    case other => other
  }.mkString(", ")

  override def toString = s"Basic stats={line count: $BodyLinesCount; top words=[$top10TokensString]}"

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: BasicBodyStats =>
      that.isInstanceOf[BasicBodyStats] &&
        that.BodyLinesCount == BodyLinesCount &&
        that.Top10Tokens == Top10Tokens
  }

  override def hashCode = (BodyLinesCount, Top10Tokens).##

  private val tokenizer = new StopWordsTokenizer
}
