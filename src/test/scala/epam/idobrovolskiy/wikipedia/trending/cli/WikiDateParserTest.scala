package epam.idobrovolskiy.wikipedia.trending.cli

import java.time.Month

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 04.08.2017.
  */
class WikiDateParserTest extends FlatSpec with Matchers {
  val parser = WikiDateParser

  "1971" should "parse correctly" in {
    parser.parse("1971") shouldEqual Some(WikiDate.AD(1971))
  }

  "123/03" should "parse correctly" in {
    parser.parse("123/03") shouldEqual Some(WikiDate.AD(123, Month.MARCH))
  }

  "2017.08.04" should "parse correctly" in {
    parser.parse("2017.08.04") shouldEqual Some(WikiDate.AD(2017, Month.AUGUST, 4))
  }

  "33BC" should "parse correctly" in {
    parser.parse("33BC") shouldEqual Some(WikiDate.BC(33))
  }

  "121.02BC" should "parse correctly" in {
    parser.parse("121.02BC") shouldEqual Some(WikiDate.BC(121, Month.FEBRUARY))
  }

  "202.12.31BC" should "parse correctly" in {
    parser.parse("202.12.31BC") shouldEqual Some(WikiDate.BC(202, Month.DECEMBER, 31))
  }

  "NOW" should "parse as WikiDate.Now" in {
    parser.parse("NOW") shouldEqual Some(WikiDate.Now)
  }

  "GENESIS" should "parse as WikiDate.MinDate" in {
    parser.parse("GENESIS") shouldEqual Some(WikiDate.MinDate)
  }
}
