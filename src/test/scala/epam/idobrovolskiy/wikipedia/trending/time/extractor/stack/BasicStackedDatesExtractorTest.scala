package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.DefaultDatesExtractor
import epam.idobrovolskiy.wikipedia.trending.time.WikiDate
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
class BasicStackedDatesExtractorTest extends FlatSpec with Matchers {

  val extractor = DefaultDatesExtractor

  "Mixin 'in'" should "work for \"In 1844\"" in {
    val result = extractor.extract("In 1844.")
    result should contain only (WikiDate.AD(1844))
  }

  "Mixin 'of'" should "work for \"During the Spanish revolution of 1873.\"" in {
    val result = extractor.extract("During the Spanish revolution of 1873.")
    result should contain only (WikiDate.AD(1873))
  }

  "Mixin 'in ... and ...'" should "work for \"In 1998 and 1999, there was\"" in {
    val result = extractor.extract("In 1998 and 1999, there was")
    result should contain only (WikiDate.AD(1998), WikiDate.AD(1999))
  }

  "<eval text>" should "<help in debugging/trying things>" in {
    println(extractor.extractWithCitations(1, "In 1998 and 1999, there was"))
  }
}
