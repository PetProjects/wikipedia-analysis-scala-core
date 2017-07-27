package epam.idobrovolskiy.wikipedia.trending.preprocessing

/**
  * Created by Igor_Dobrovolskiy on 27.07.2017.
  */
trait PlainTextExtractor {
  def extract(inPath: String, outPath: String)
}
