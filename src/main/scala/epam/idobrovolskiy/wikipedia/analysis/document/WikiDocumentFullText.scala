package epam.idobrovolskiy.wikipedia.analysis.document

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */

class WikiDocumentFullText
(
  id: Int,
  title: String,
  url: String,
  stats: BasicBodyStats,
  val fullText: IndexedSeq[String]
)
  extends WikiDocumentWithBasicBodyStats(id, title, url, stats)
