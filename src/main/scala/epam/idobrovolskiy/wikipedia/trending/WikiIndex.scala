package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.indexing.WikiDocumentIndexer

/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object WikiIndex extends App {

  if (args.contains("--v2") || args.contains("--dr"))
    WikiDocumentIndexer.buildDateRangesDocIndex(args.contains("--debug"))
  else
    WikiDocumentIndexer.buildIndex(args.contains("--debug"))

  spark.stop()
}
