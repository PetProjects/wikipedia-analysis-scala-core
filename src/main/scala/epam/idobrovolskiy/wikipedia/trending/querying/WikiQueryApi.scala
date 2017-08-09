package epam.idobrovolskiy.wikipedia.trending.querying

import epam.idobrovolskiy.wikipedia.trending.cli.TokensForPeriodQueryArgs
import epam.idobrovolskiy.wikipedia.trending.indexing.WikiDocumentIndexer
import epam.idobrovolskiy.wikipedia.trending.time.WikiDate
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{min, max, explode, sum}

/**
  * Created by Igor_Dobrovolskiy on 02.08.2017.
  */
object WikiQueryApi {

  def queryTokensForPeriod(args: TokensForPeriodQueryArgs): Seq[String] = {
    val df = getDocsForPeriod(args.since, args.until, args.debug)

    if (args.debug) {
      df.printSchema()
      println(s"Total distinct documents (by doc index) which matched query: ${df.count}")
    }

    import df.sqlContext.implicits._

    val tokenDf = df.select(explode('top_tokens) as Array("token", "tcounts"))
      .groupBy('token).agg(sum('tcounts).alias("total_count"))
      .orderBy('total_count.desc)

    if (args.debug) {
      tokenDf.printSchema()
      tokenDf.show(30)
    }

    val tokens = tokenDf
      .take(args.topN)
      .map(r => s"  [${r(1)}]\t\t${r(0)}")
      .toList

    tokens
  }

  private def datesForPeriodDebugInfo(df: DataFrame, sinceSer: Long, untilSer: Long) = {
    df.printSchema()
    df.show(10)

    import df.sqlContext.implicits._

    println(s"query args: since(ser)=$sinceSer; until(ser)=$untilSer")
    val distDocCount = df.select('wiki_id).distinct.count
    println(s"Total distinct documents (by dates index) which matched query: $distDocCount")

    val (minKey, maxKey) = df.select(min('wiki_date), max('wiki_date)).collect
      .map(r => (r.getLong(0), r.getLong(1))).head

    println(s"Whole index info:\n  min date(ser)=$minKey; max date(ser)=$maxKey")

    val altDistDocCount = df.where('wiki_date >= minKey && 'wiki_date <= maxKey).select('wiki_id).distinct.count
    println(s"  distinct doc count for actual [min date..max date]=$altDistDocCount")

    val futureDocs = df.where('wiki_date > untilSer)
    println(s"Some wiki docs from the future (total count=${futureDocs.count}):")
    futureDocs.show(30)
  }

  private def getDatesForPeriod(since: WikiDate, until: WikiDate, debug: Boolean): DataFrame = {
    val df = WikiDocumentIndexer.getDateIndexFile()

    val sinceSer = since.serialize
    val untilSer = until.serialize

    if (debug)
      datesForPeriodDebugInfo(df, sinceSer, untilSer)

    import df.sqlContext.implicits._

    df.where('wiki_date >= sinceSer && 'wiki_date <= untilSer)
  }

  private def getAllDocs: DataFrame = WikiDocumentIndexer.getDocIndexFile()

  private def getDocsForPeriod(since: WikiDate, until: WikiDate, debug: Boolean): DataFrame = {
    val df = getDatesForPeriod(since, until, debug)

    import df.sqlContext.implicits._

    val ids = df.select('wiki_id).distinct()
    val docs = getAllDocs

    ids.join(docs, ids("wiki_id") === docs("wiki_id")).drop(ids("wiki_id"))
  }
}
