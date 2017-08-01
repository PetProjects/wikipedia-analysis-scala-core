package epam.idobrovolskiy.wikipedia.trending.cli

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQueryArgsParser {
  val usage =
    """ Usage:
      | WikiQuery (--tokens|--articles [{HDF} | {HDF}-{HDF} | {HDF}-[NOW] | [GENESIS]-{HDF}] )
      |    | (--distribution {token})
      |    | --help
      |
      |  {HDF}={historical date format} Examples:
      |    1971 - treated as 1971 year
      |    331BC - treated as 331 year BC
      |    1431-03 - treated as March of 1431 year
      |    1941-06-22 - treated as June 22, 1941
      |    NOW - treated as current date (year, month and day).
      |        Could be skipped in time range, for UNTIL position.
      |    GENESIS - earliest time, i.e. the oldest time mentioned in Wikipedia.
      |        Could be skipped in time range, for SINCE position.
      |
      |  {HDF}-{HDF} among simple case of specifying valid ranges of
      |        proper SINCE followed by UNTIL {HDF} values, could be:
      |    1730s - for decade 1730-1739
      |    {X}th[BC] - for {X}th century, e.g. 5th for 500-599 or 3thBC for [399 BC..300 BC]
      |    {X}Kya - for {X} thousand years ago, e.g. 5Kya for ~[XXX BC .. XXI BC]
      |    AD - treated as period [1 AD..NOW]
      |    BC - treated as period [{}..1 BC]
      |
      | CLI Examples:
      |    WikiQuery --tokens 1901-NOW
      |    WikiQuery --articles BC
      |    WikiQuery --articles 2000-
      |    WikiQuery --tokens 1970s
      |    WikiQuery --distribution Sumer
      |
      | NOTE: Most of the mentioned usage cases are only planned for implementation,
      |       and just the simplest queries are supported atm.
    """.stripMargin


  private def parseTokensArgs(tail: List[String]): Option[WikiQueryArgs] = ???
  private def parseArticlesArgs(tail: List[String]): Option[WikiQueryArgs] = ???
  private def parseDistributionArgs(tail: List[String]): Option[WikiQueryArgs] = ???

  private def parseOptions(argList: List[String]): Option[WikiQueryArgs] = {
    argList match {
      case Nil =>
        None //no args specified is treated unexpected for WikiQuery
      case "--tokens" :: tail =>
        parseTokensArgs(tail)
      case "--articles" :: tail =>
        parseArticlesArgs(tail)
      case "--distribution" :: tail =>
        parseDistributionArgs(tail)
      case "--help" :: _ =>
        None
      case option :: _ =>
        println("Unknown option: " + option); None
    }
  }

  def parse(args: Array[String]): Option[WikiQueryArgs] =
    parseOptions(args.toList) match {
      case x @ Some(_) => x
      case _ => printUsage(); None
    }

  def printUsage() = println(usage)
}
