package epam.idobrovolskiy.wikipedia.trending.tokenizer

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
class StopWordsTokenizer extends Tokenizer {

  override def splitWords(s: String): Seq[String] =
    //s.toLowerCase.split("""[\s,\.!?:\<\>=/'"\(\)-\u2013]+|(%\d{2})""").filter(_.length > 0) //TODO: implement better tags handling
    s.toLowerCase.split("""[\s,\.!?:\<\>=/'"\(\)-]+|(%\d{2})""").filter(_.length > 0) //TODO: implement better tags handling

  override def filterWords(w: String): Boolean =
    ! StopWordsTokenizer.stopWords.contains(w)
}

object StopWordsTokenizer {
  lazy private val stopWords: Set[String] = //TODO: Rework into file/resource based dictionary
    Set("a", "an", "the", "of", "to", "at", "for",
      "in", "was", "were", "as", "by", "and", "with", "over",
      "he", "his", "him", "she", "her", "from", "on", "but", "it", "its",
      "they", "that", "this", "these", "those", "we", "us", "me", "my",
      "have", "has", "had", "will", "would", "shall", "should", "can", "could", //Deliberate what to do with "may" not to confuse with month .. take into account first upper "M"?
      "against", "which", "whose", "who", "whom", "what", "when", "where",
      "be", "are", "is", "been", "or", "no", "not", "used",
      "one", "ones", "two", "all", "any", "another", "other", "such", "also",
      "if", "else", "then", "than",
      "each", "some", "more", "most", "less", "least", "best", "better", "worst", "worse",
      "la", "de", "s",
      "u", "q", //what are these?
      "href", "http%3a", "https%3a") //TODO: remove when better tags handling is implemented
}