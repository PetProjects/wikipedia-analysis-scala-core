package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending._
import epam.idobrovolskiy.wikipedia.trending.time.{WikiDate, WikiDateRange}

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQueryArgsParser {
  val usage =
    s"$AppName - $AppVersion" +
    """
      |Usage:
      | WikiQuery (--tokens|--articles [{HDF} | {HDF}-{HDF} | {HDF}-[NOW] | [GENESIS]-{HDF}] )
      |    | (--distribution {token})
      |    [--help] [--debug]
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


  private def parseTokensArgs(argList: List[String], defArgs: WikiQueryArgs): Option[WikiQueryArgs] =
    argList match {
      case Nil => Some(defArgs)
      case "--tokens" :: tail => //setting up default time frame at first, then it will be overriden by specific dates, once specified.
        parseTokensArgs(tail, TokensForPeriodQueryArgs(WikiDate.MinDate, WikiDate.Now, debug = defArgs.debug))
      case dates :: tail if ! dates.startsWith("--") => { //anything, not an option, must be date or date range
        WikiDateRangeParser.parse(dates) match {
          case Some(range) =>
            parseTokensArgs(tail, TokensForPeriodQueryArgs(range.since, range.until, debug = defArgs.debug))

          case None => {
//            println(s"\"${dates}\" is invalid date/date range.")
            None
          } //parsing failed
        }
      }
      case s :: tail if s.startsWith("--") => //as it is an option, it should be a common option, so it's skipped (as already processed in parseCommonOptions())
        parseTokensArgs(tail, defArgs)

      //no other cases are expected, so we are ok to crash with NoSuchMethodException
    }

  private def parseArticlesArgs(argList: List[String], defArgs: WikiQueryArgs): WikiQueryArgs = ???

  private def parseDistributionArgs(argList: List[String], defArgs: WikiQueryArgs): WikiQueryArgs = ???

  def duplicateQueryType: WikiQueryArgs = {
    println("Multiple query types in single query are not allowed.")
    InvalidArgs
  }

  def argsForQueryType(tail: List[String], defArgs: WikiQueryArgs, qType: WikiQueryType.Value) =
    if (defArgs.queryType == null || defArgs.queryType == qType)
      parseCommonOptions(
        tail,
        new WikiQueryArgs {
          override val queryType = qType
          override val debug = defArgs.debug
        }
      )
    else
      duplicateQueryType

  private def parseCommonOptions(argList: List[String], defArgs: WikiQueryArgs): WikiQueryArgs = {
    argList match {
      case Nil => defArgs

      case "--tokens" :: tail => argsForQueryType(tail, defArgs, WikiQueryType.TopTokensForPeriod)

      case "--articles" :: tail => argsForQueryType(tail, defArgs, WikiQueryType.TopDocumentsForPeriod)

      case "--distribution" :: tail => argsForQueryType(tail, defArgs, WikiQueryType.PeriodDistributionForToken)

      case "--debug" :: tail =>
        parseCommonOptions(tail, new WikiQueryArgs {
          override val debug = true
          override val queryType = defArgs.queryType
        })

      case "--help" :: _ => InvalidArgs

      case _ :: tail => //not a common option, leaving it for specific parser
        parseCommonOptions(tail, defArgs)
    }
  }

  val InvalidArgs = new WikiQueryArgs {
    val queryType = null
  }

  def parse(args: Array[String]): Option[WikiQueryArgs] = {
    val argList = args.toList
    val commonOpts = parseCommonOptions(argList,
      InvalidArgs //no args specified is treated unexpected for WikiQuery, i.e. at least query type must be specified explicitly
    )

    val cOpts = commonOpts.queryType match {
      case WikiQueryType.TopTokensForPeriod => parseTokensArgs(argList, commonOpts)
      case WikiQueryType.TopDocumentsForPeriod => Some(parseArticlesArgs(argList, commonOpts))
      case WikiQueryType.PeriodDistributionForToken => Some(parseDistributionArgs(argList, commonOpts))

      case _ => None
    }

    cOpts match { //print usage if failed
      case None => { printUsage(); None }
      case x => x
    }
  }

  def printUsage() = println(usage)
}
