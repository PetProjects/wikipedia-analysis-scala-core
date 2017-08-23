package epam.idobrovolskiy.wikipedia.trending.querying

import epam.idobrovolskiy.wikipedia.trending.cli.TokensForPeriodQueryArgs
import org.apache.spark.sql.SparkSession

/**
  * Created by Igor_Dobrovolskiy on 18.08.2017.
  */
object WikiHiveQueryApi extends WikiBaseQuery {

  def getHiveQueryFor(args: TokensForPeriodQueryArgs): String = {
    val sinceSer = args.since.serialize
    val untilSer = args.until.serialize

    """ select
      |    tt.token, sum(tt.tcount) as total_tcount
      |from (
      |    select t.top_tokens
      |    from wikitrending.data_range_hv t
      |    lateral view inline(t.date_range) drs as since, until
      |    where t.min_date <= %d and t.max_date >= %d and drs.since <= %d and drs.until >= %d
      |) t
      |lateral view explode(t.top_tokens) tt as token, tcount
      |group by tt.token
      |order by total_tcount desc
      |limit %d
      |""".format(untilSer, sinceSer, untilSer, sinceSer, args.topN).stripMargin
  }

  def queryTokensForPeriod(sparkSession: SparkSession, args: TokensForPeriodQueryArgs): Seq[String] = {

    import sparkSession.sql

    val query = getHiveQueryFor(args)

    formatQueryOutput(sql(query).collect())
  }
}
