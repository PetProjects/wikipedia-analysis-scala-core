import epam.idobrovolskiy.wikipedia.analysis._

/**
  * Created by hp on 29.06.2017.
  */

object Main extends App {
  val DefaultFilePath = "../../../in/20170501/out/"
  //    "D:/work/.pdp/scala-studing/scala-spark-demo-project/wikipedia-analysis/in/20170501/out/AA/wiki_00"
  //    "wiki_small"

  val docProducer = new AttardiWikiDocumentProducer
  val path = if (args.length > 0) args(0) else DefaultFilePath

  docProducer.getDocuments(path).foreach(x => println(x))
}
