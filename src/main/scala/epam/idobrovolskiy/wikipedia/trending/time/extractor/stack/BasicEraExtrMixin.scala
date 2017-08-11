package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

/**
  * Created by Igor_Dobrovolskiy on 11.08.2017.
  */
trait BasicEraExtrMixin {
  private val TextToCommonEra = Map[String, Boolean](
    "ad" -> true,
    "a.d." -> true,
    "bc" -> false,
    "b.c." -> false,
    "ce" -> true,
    "c.e." -> true,
    "bce" -> false,
    "b.c.e." -> false
  )

  protected val reEra = TextToCommonEra.keySet.map(_.replace(".", "\\.")).mkString("(", "|", ")")

  def textToCommonEra(text: String): Option[Boolean] = TextToCommonEra.get(text)
}
