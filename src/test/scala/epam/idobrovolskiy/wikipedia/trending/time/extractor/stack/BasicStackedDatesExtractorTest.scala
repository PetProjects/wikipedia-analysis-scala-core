package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import java.time.Month

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

  "Mixin 'in <month> <year>'" should "work for \"in December 1847\"" in {
    val result = extractor.extract("in December 1847")
    result should contain only (WikiDate.AD(1847, Month.DECEMBER))
  }

  it should "work for \"He divorced his wife, <a href=\"Irma%20Raush\">Irma Raush</a>, in June 1970.\" too" in {
    val result = extractor.extract(""""He divorced his wife, <a href="Irma%20Raush">Irma Raush</a>, in June 1970."""")
    result should contain only (WikiDate.AD(1970, Month.JUNE))
  }

  "Mixin 'in <month> <day> <year>'" should "work for \"On January 30, 1846, the Alabama legislature announced\"" in {
    val result = extractor.extract("On January 30, 1846, the Alabama legislature announced")
    result should contain only (WikiDate.AD(1846, Month.JANUARY, 30))
  }

  it should "work for \"presented on June 10 of 2008 in\" too" in {
    val result = extractor.extract("presented on June 10 of 2008 in")
    result should contain only (WikiDate.AD(2008, Month.JUNE, 10))
  }

  it should "work for \"had originally been announced on May 21, 1984\" as well" in {
    val result = extractor.extract("had originally been announced on May 21, 1984")
    result should contain only (WikiDate.AD(1984, Month.MAY, 21))
  }

  "<eval text>" should "<help in debugging/trying things>" in {
    println(extractor.extractWithCitations(1, "In 1998 and 1999, there was"))
  }
}
