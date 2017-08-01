package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.indexing.WikiDocumentIndexer

/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object WikiIndex extends App {

  WikiDocumentIndexer.buildIndex

  spark.stop()
}
