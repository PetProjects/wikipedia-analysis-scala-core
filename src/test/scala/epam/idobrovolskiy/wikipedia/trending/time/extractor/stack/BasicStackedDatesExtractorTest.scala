package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import java.time.Month

import epam.idobrovolskiy.wikipedia.trending.DefaultDatesExtractor
import epam.idobrovolskiy.wikipedia.trending.time.{WikiDate, WikiDateRange}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
class BasicStackedDatesExtractorTest extends FlatSpec with Matchers {

  val extractor = DefaultDatesExtractor

  "Mixin 'in <year>'" should "extract date for \"In 1844\"" in {
    val result = extractor.extractDates("In 1844.")
    result should contain only (WikiDate.AD(1844))
  }

  it should "extract range for \"In 1844\"" in {
    val result = extractor.extractRanges("In 1844.")
    result should contain only WikiDateRange(WikiDate.AD(1844), WikiDate.AD(1844, Month.DECEMBER, 31))
  }

  "Mixin 'of <year>'" should "extract date for \"During the Spanish revolution of 1873.\"" in {
    val result = extractor.extractDates("During the Spanish revolution of 1873.")
    result should contain only (WikiDate.AD(1873))
  }

  it should "extract range for \"During the Spanish revolution of 1873.\"" in {
    val result = extractor.extractRanges("During the Spanish revolution of 1873.")
    result should contain only WikiDateRange(WikiDate.AD(1873), WikiDate.AD(1873, Month.DECEMBER, 31))
  }

  "Mixin 'in <year> and <year>'" should "extract two dates for \"In 1998 and 1999, there was\"" in {
    val result = extractor.extractDates("In 1998 and 1999, there was")
    result should contain only (WikiDate.AD(1998), WikiDate.AD(1999))

  }

  it should "extract two ranges for \"In 1998 and 1999, there was\"" in {
    val result = extractor.extractRanges("In 1998 and 1999, there was")
    result should contain only (
      WikiDateRange(WikiDate.AD(1998), WikiDate.AD(1998, Month.DECEMBER, 31)),
      WikiDateRange(WikiDate.AD(1999), WikiDate.AD(1999, Month.DECEMBER, 31))
    )
  }

  "Mixin 'in <month> <year>'" should "extract date for \"in December 1847\"" in {
    val result = extractor.extractDates("in December 1847")
    result should contain only (WikiDate.AD(1847, Month.DECEMBER))
  }

  it should "extract range for \"in December 1847\"" in {
    val result = extractor.extractRanges("in December 1847")
    result should contain only WikiDateRange(WikiDate.AD(1847, Month.DECEMBER), WikiDate.AD(1847, Month.DECEMBER, 31))
  }

  it should "extract date for \"He divorced his wife, <a href=\"Irma%20Raush\">Irma Raush</a>, in June 1970.\" too" in {
    val result = extractor.extractDates(""""He divorced his wife, <a href="Irma%20Raush">Irma Raush</a>, in June 1970."""")
    result should contain only (WikiDate.AD(1970, Month.JUNE))
  }

  it should "extract range for \"He divorced his wife, <a href=\"Irma%20Raush\">Irma Raush</a>, in June 1970.\" too" in {
    val result = extractor.extractRanges(""""He divorced his wife, <a href="Irma%20Raush">Irma Raush</a>, in June 1970."""")
    result should contain only WikiDateRange(WikiDate.AD(1970, Month.JUNE), WikiDate.AD(1970, Month.JUNE, 30))
  }

  "Mixin 'on <month> <day> <year>'" should "extract date for \"On January 30, 1846, the Alabama legislature announced\"" in {
    val result = extractor.extractDates("On January 30, 1846, the Alabama legislature announced")
    result should contain only (WikiDate.AD(1846, Month.JANUARY, 30))
  }

  it should "extract range for \"On January 30, 1846, the Alabama legislature announced\"" in {
    val result = extractor.extractRanges("On January 30, 1846, the Alabama legislature announced")
    val date = WikiDate.AD(1846, Month.JANUARY, 30)
    result should contain only WikiDateRange(date, date)
  }

  it should "extract date for \"presented on June 10 of 2008 in\" too" in {
    val result = extractor.extractDates("presented on June 10 of 2008 in")
    result should contain only (WikiDate.AD(2008, Month.JUNE, 10))
  }

  it should "extract range for \"presented on June 10 of 2008 in\" too" in {
    val result = extractor.extractRanges("presented on June 10 of 2008 in")
    val date = WikiDate.AD(2008, Month.JUNE, 10)
    result should contain only WikiDateRange(date, date)
  }

  it should "extract date for \"had originally been announced on May 21, 1984\" as well" in {
    val result = extractor.extractDates("had originally been announced on May 21, 1984")
    result should contain only (WikiDate.AD(1984, Month.MAY, 21))
  }

  it should "extract range for \"had originally been announced on May 21, 1984\" as well" in {
    val result = extractor.extractRanges("had originally been announced on May 21, 1984")
    val date = WikiDate.AD(1984, Month.MAY, 21)
    result should contain only WikiDateRange(date, date)
  }

  it should "not raise exception for incorrect dates, such as \"Nothing could happen on November 31, 1982.\" and parse out no date" in {
    val result = extractor.extractDates("Nothing could happen on November 31, 1982.")
    result shouldBe empty
  }

  it should "not raise exception for incorrect dates, such as \"Nothing could happen on November 31, 1982.\" and parse out no range" in {
    val result = extractor.extractRanges("Nothing could happen on November 31, 1982.")
    result shouldBe empty
  }

  "Mixin '<year> <era>'" should "extract date for \"in her pilgrimage in 326 CE.\"" in {
    val result = extractor.extractDates("in her pilgrimage in 326 CE.")
    result should contain only (WikiDate.AD(326))
  }

  it should "extract range for \"in her pilgrimage in 326 CE.\"" in {
    val result = extractor.extractRanges("in her pilgrimage in 326 CE.")
    result should contain only WikiDateRange(WikiDate.AD(326), WikiDate.AD(326, Month.DECEMBER, 31))
  }

  it should "extract date for \"King Satakarni (241 BC), the son of \"" in {
    val result = extractor.extractDates("King Satakarni (241 BC), the son of ")
    result should contain only (WikiDate.BC(241))
  }

  it should "extract range for \"King Satakarni (241 BC), the son of \"" in {
    val result = extractor.extractRanges("King Satakarni (241 BC), the son of ")
    result should contain only WikiDateRange(WikiDate.BC(241), WikiDate.BC(241, Month.DECEMBER, 31))
  }

  it should "extract date for \" in approximately 7000 BC.\"" in {
    val result = extractor.extractDates(" in approximately 7000 BC.")
    result should contain only (WikiDate.BC(7000))
  }

  it should "extract range for \" in approximately 7000 BC.\"" in {
    val result = extractor.extractRanges(" in approximately 7000 BC.")
    result should contain only WikiDateRange(WikiDate.BC(7000), WikiDate.BC(7000, Month.DECEMBER, 31))
  }

  it should "extract date for \"was born around 1500 BC.\"" in {
    val result = extractor.extractDates("was born around 1500 BC.")
    result should contain only (WikiDate.BC(1500))
  }

  it should "extract range for \"was born around 1500 BC.\"" in {
    val result = extractor.extractRanges("was born around 1500 BC.")
    result should contain only WikiDateRange(WikiDate.BC(1500), WikiDate.BC(1500, Month.DECEMBER, 31))
  }

  it should "extract date for \"Meanwhile, around 1180 BC, Moses was born in Egypt\"" in {
    val result = extractor.extractDates("Meanwhile, around 1180 BC, Moses was born in Egypt")
    result should contain only (WikiDate.BC(1180))
  }

  it should "extract range for \"Meanwhile, around 1180 BC, Moses was born in Egypt\"" in {
    val result = extractor.extractRanges("Meanwhile, around 1180 BC, Moses was born in Egypt")
    result should contain only WikiDateRange(WikiDate.BC(1180), WikiDate.BC(1180, Month.DECEMBER, 31))
  }

  it should "extract date for \"In 32 BC, Jesus Christ was born in Israel.\"" in {
    val result = extractor.extractDates("In 32 BC, Jesus Christ was born in Israel.")
    result should contain only (WikiDate.BC(32))
  }

  it should "extract range for \"In 32 BC, Jesus Christ was born in Israel.\"" in {
    val result = extractor.extractRanges("In 32 BC, Jesus Christ was born in Israel.")
    result should contain only WikiDateRange(WikiDate.BC(32), WikiDate.BC(32, Month.DECEMBER, 31))
  }

  it should "extract date for \"In 1046 B.C., \"" in {
    val result = extractor.extractDates("In 1046 B.C., ")
    result should contain only (WikiDate.BC(1046))
  }

  it should "extract range for \"In 1046 B.C., \"" in {
    val result = extractor.extractRanges("In 1046 B.C., ")
    result should contain only WikiDateRange(WikiDate.BC(1046), WikiDate.BC(1046, Month.DECEMBER, 31))
  }

  it should "extract date for \"upon sacking Napata in 591 BCE under the reign\"" in {
    val result = extractor.extractDates("upon sacking Napata in 591 BCE under the reign")
    result should contain only (WikiDate.BC(591))
  }

  it should "extract range for \"upon sacking Napata in 591 BCE under the reign\"" in {
    val result = extractor.extractRanges("upon sacking Napata in 591 BCE under the reign")
    result should contain only WikiDateRange(WikiDate.BC(591), WikiDate.BC(591, Month.DECEMBER, 31))
  }

  it should "extract date for \"having been drilled dating to 7,000 B.C.E.\"" in {
    val result = extractor.extractDates("having been drilled dating to 7,000 B.C.E.")
    result should contain only (WikiDate.BC(7000))
  }

  it should "extract range for \"having been drilled dating to 7,000 B.C.E.\"" in {
    val result = extractor.extractRanges("having been drilled dating to 7,000 B.C.E.")
    result should contain only WikiDateRange(WikiDate.BC(7000), WikiDate.BC(7000, Month.DECEMBER, 31))
  }

  it should "extract date for \"In 17,000 B.C.E., one of them defied his brothers\"" in {
    val result = extractor.extractDates("In 17,000 B.C.E., one of them defied his brothers")
    result should contain only (WikiDate.BC(17000))
  }

  it should "extract range for \"In 17,000 B.C.E., one of them defied his brothers\"" in {
    val result = extractor.extractRanges("In 17,000 B.C.E., one of them defied his brothers")
    result should contain only WikiDateRange(WikiDate.BC(17000), WikiDate.BC(17000, Month.DECEMBER, 31))
  }

  it should "extract date for \"Second Council of Constantinople</a> (553 C.E.) not only called him\"" in {
    val result = extractor.extractDates("Second Council of Constantinople</a> (553 C.E.) not only called him")
    result should contain only (WikiDate.AD(553))
  }

  it should "extract range for \"Second Council of Constantinople</a> (553 C.E.) not only called him\"" in {
    val result = extractor.extractRanges("Second Council of Constantinople</a> (553 C.E.) not only called him")
    result should contain only WikiDateRange(WikiDate.AD(553), WikiDate.AD(553, Month.DECEMBER, 31))
  }

  it should "extract date for \"Epictetus was born c. 50 A.D., presumably at\"" in {
    val result = extractor.extractDates("Epictetus was born c. 50 A.D., presumably at")
    result should contain only (WikiDate.AD(50))
  }

  it should "extract range for \"Epictetus was born c. 50 A.D., presumably at\"" in {
    val result = extractor.extractRanges("Epictetus was born c. 50 A.D., presumably at")
    result should contain only WikiDateRange(WikiDate.AD(50), WikiDate.AD(50, Month.DECEMBER, 31))
  }

  it should "extract date for \"studied under him when a young man (c. 108 A.D.)\"" in {
    val result = extractor.extractDates("studied under him when a young man (c. 108 A.D.)")
    result should contain only (WikiDate.AD(108))
  }

  it should "extract range for \"studied under him when a young man (c. 108 A.D.)\"" in {
    val result = extractor.extractRanges("studied under him when a young man (c. 108 A.D.)")
    result should contain only WikiDateRange(WikiDate.AD(108), WikiDate.AD(108, Month.DECEMBER, 31))
  }

  it should "extract date for \"China was recorded in 166 AD by the\"" in {
    val result = extractor.extractDates("China was recorded in 166 AD by the")
    result should contain only (WikiDate.AD(166))
  }

  it should "extract range for \"China was recorded in 166 AD by the\"" in {
    val result = extractor.extractRanges("China was recorded in 166 AD by the")
    result should contain only WikiDateRange(WikiDate.AD(166), WikiDate.AD(166, Month.DECEMBER, 31))
  }

  it should "not parse \"some id 12 A.D, not a date\" as valid date (return no date)" in {
    val result = extractor.extractDates("some id 12 A.D, not a date")
    result shouldBe empty
  }

  it should "not parse \"some id 12 A.D, not a date\" as valid range (return no range)" in {
    val result = extractor.extractRanges("some id 12 A.D, not a date")
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
    println(extractor.extractDatesWithCitations(1, "In 1998 and 1999, there was"))
  }
}
