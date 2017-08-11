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

  "Mixin 'in <year>'" should "work for \"In 1844\"" in {
    val result = extractor.extract("In 1844.")
    result should contain only (WikiDate.AD(1844))
  }

  "Mixin 'of <year>'" should "work for \"During the Spanish revolution of 1873.\"" in {
    val result = extractor.extract("During the Spanish revolution of 1873.")
    result should contain only (WikiDate.AD(1873))
  }

  "Mixin 'in <year> and <year>'" should "work for \"In 1998 and 1999, there was\"" in {
    val result = extractor.extract("In 1998 and 1999, there was")
    result should contain only(WikiDate.AD(1998), WikiDate.AD(1999))
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

  it should "not raise exception for incorrect dates, such as \"Nothing could happen on November 31, 1982.\" and parse out no date" in {
    val result = extractor.extract("Nothing could happen on November 31, 1982.")
    result shouldBe empty
  }

  "Mixin '<year> <era>'" should "work for \"in her pilgrimage in 326 CE.\"" in {
    val result = extractor.extract("in her pilgrimage in 326 CE.")
    result should contain only (WikiDate.AD(326))
  }

  it should "work for \"King Satakarni (241 BC), the son of \"" in {
    val result = extractor.extract("King Satakarni (241 BC), the son of ")
    result should contain only (WikiDate.BC(241))
  }

  it should "work for \" in approximately 7000 BC.\"" in {
    val result = extractor.extract(" in approximately 7000 BC.")
    result should contain only (WikiDate.BC(7000))
  }

  it should "work for \"was born around 1500 BC.\"" in {
    val result = extractor.extract("was born around 1500 BC.")
    result should contain only (WikiDate.BC(1500))
  }

  it should "work for \"Meanwhile, around 1180 BC, Moses was born in Egypt\"" in {
    val result = extractor.extract("Meanwhile, around 1180 BC, Moses was born in Egypt")
    result should contain only (WikiDate.BC(1180))
  }

  it should "work for \"In 32 BC, Jesus Christ was born in Israel.\"" in {
    val result = extractor.extract("In 32 BC, Jesus Christ was born in Israel.")
    result should contain only (WikiDate.BC(32))
  }

  it should "work for \"In 1046 B.C., \"" in {
    val result = extractor.extract("In 1046 B.C., ")
    result should contain only (WikiDate.BC(1046))
  }

  it should "work for \"upon sacking Napata in 591 BCE under the reign\"" in {
    val result = extractor.extract("upon sacking Napata in 591 BCE under the reign")
    result should contain only (WikiDate.BC(591))
  }

  it should "work for \"having been drilled dating to 7,000 B.C.E.\"" in {
    val result = extractor.extract("having been drilled dating to 7,000 B.C.E.")
    result should contain only (WikiDate.BC(7000))
  }

  it should "work for \"In 17,000 B.C.E., one of them defied his brothers\"" in {
    val result = extractor.extract("In 17,000 B.C.E., one of them defied his brothers")
    result should contain only (WikiDate.BC(17000))
  }

  it should "work for \"Second Council of Constantinople</a> (553 C.E.) not only called him\"" in {
    val result = extractor.extract("Second Council of Constantinople</a> (553 C.E.) not only called him")
    result should contain only (WikiDate.AD(553))
  }

  it should "work for \"Epictetus was born c. 50 A.D., presumably at\"" in {
    val result = extractor.extract("Epictetus was born c. 50 A.D., presumably at")
    result should contain only (WikiDate.AD(50))
  }

  it should "work for \"studied under him when a young man (c. 108 A.D.)\"" in {
    val result = extractor.extract ("studied under him when a young man (c. 108 A.D.)")
    result should contain only (WikiDate.AD(108))
  }

  it should "work for \"China was recorded in 166 AD by the\"" in {
    val result = extractor.extract ("China was recorded in 166 AD by the")
    result should contain only (WikiDate.AD(166))
  }

  it should "not parse \"some id 12 A.D, not a date\" as valid date (return no date)" in {
    val result = extractor.extract("some id 12 A.D, not a date")
    result shouldBe empty
  }

  "ExtractionLogger" should "return WikiDate.NoDate in case of exception during the flow" in {
    val logger = new ExtractionLogger {
      override def logExtractionIssue(issueDescription: String): Unit = {}
    }
    val fakeMatch = ("""a""".r findAllIn "a").matchData.toList.head
    val ext = logger.tryExtract(-1, fakeMatch) {
      throw new Exception
    }

    ext.date shouldEqual WikiDate.NoDate
  }

  "<eval text>" should "<help in debugging/trying things>" in {
    println(extractor.extractWithCitations(1, "In 1998 and 1999, there was"))
  }
}
