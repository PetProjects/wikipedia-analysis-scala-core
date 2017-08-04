package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.time.{WikiDate, WikiDateRange}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 04.08.2017.
  */
class WikiDateRangeParserTest extends FlatSpec with Matchers {
  val parser = WikiDateRangeParser

  "1969" should "parse as [1969-1970), i.e. till 1970 exclusive" in {
    parser.parse("1969") shouldEqual Some(WikiDateRange(WikiDate.AD(1969), WikiDate.AD(1970)))
  }
}
