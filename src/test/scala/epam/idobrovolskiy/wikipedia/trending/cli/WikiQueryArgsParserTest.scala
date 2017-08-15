package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 03.08.2017.
  */
class WikiQueryArgsParserTest extends FlatSpec with Matchers {
  "CLI w/o any option" should "not be parsed as valid options" in {
    val args = Array.empty[String]

    WikiQueryArgsParser.parse(args) shouldEqual None
  }

  "CLI with single '--debug' option" should "not be parsed as valid options" in {
    val args = Array("--debug")

    WikiQueryArgsParser.parse(args) shouldEqual None
  }

  "CLI with single '--tokens' option" should "be parsed as TokensForPeriodQueryArgs with timeframe from Genesis till Now" in {
    val args = Array("--tokens")

    WikiQueryArgsParser.parse(args) shouldBe Some(TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now))
  }

  val tokensDebugExpected =
    Some(TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now, debug = true))

  "CLI with '--tokens' and '--debug' option" should "be parsed as TokensForPeriodQueryArgs with timeframe from Genesis till Now and debug flag ON" in {
    val args = Array("--tokens", "--debug")

    WikiQueryArgsParser.parse(args) shouldEqual tokensDebugExpected
  }

  it should "work the same when options are placed in reversed order" in {
    val args = Array("--debug", "--tokens")

    WikiQueryArgsParser.parse(args) shouldEqual tokensDebugExpected
  }

  "CLI with '--tokens' and '--version=1' option" should "be parsed properly" in {
    val args = Array("--tokens","--version=1")

    WikiQueryArgsParser.parse(args) shouldEqual Some(TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now, debug = false, queryVersion = 1))
  }

  it should "work the same when options are placed in reversed order" in {
    val args = Array("--version=1", "--tokens")

    WikiQueryArgsParser.parse(args) shouldEqual Some(TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now, debug = false, queryVersion = 1))
  }

  "CLI with '--tokens' and '--version=2' option" should "be parsed properly" in {
    val args = Array("--tokens","--version=2")

    WikiQueryArgsParser.parse(args) shouldEqual Some(TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now, debug = false, queryVersion = 2))
  }
}
