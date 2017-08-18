package epam.idobrovolskiy.wikipedia.trending.querying

import org.apache.spark.sql.Row

/**
  * Created by Igor_Dobrovolskiy on 18.08.2017.
  */
trait WikiBaseQuery {
  def formatQueryOutput(rs: Array[Row]): Seq[String] =
    rs.map(r => s"  [${r(1)}]\t\t${r(0)}").toList
}
