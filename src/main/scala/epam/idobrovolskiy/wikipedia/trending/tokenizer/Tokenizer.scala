package epam.idobrovolskiy.wikipedia.trending.tokenizer

import epam.idobrovolskiy.wikipedia.trending.TopTokenCount
import scala.collection.mutable

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */

trait Tokenizer {

  def splitWords(s: String): Seq[String]

  def filterWords(w: String): Boolean

  private def tokenizeLine(s: String): Map[String, Int] =
    splitWords(s).filter(filterWords(_)).groupBy(w => w).map { case (w, ws) => (w, ws.length) }

  def tokenize(ss: Seq[String]): Map[String, Int] =
    if (ss.isEmpty)
      Map.empty[String, Int]
    else {
      val tMap = mutable.Map.empty[String, Int]
      for {
        s <- ss
        (k, count) <- tokenizeLine(s)
      }
        if (tMap.contains(k))
          tMap(k) += count
        else
          tMap += k -> count

      tMap.toMap
    }

  def getTopNTokens(ss: Seq[String], topN: Int = TopTokenCount) =
    tokenize(ss)
      .toList
      .sortBy(-_._2)
      .take(topN)
      .toMap

}
