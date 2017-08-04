package epam.idobrovolskiy.wikipedia.trending.tokenizer

import org.scalatest.{BeforeAndAfter, FlatSpec, MustMatchers}

/**
  * Created by Igor_Dobrovolskiy on 03.08.2017.
  */
class StopWordsTokenizerTest extends FlatSpec with MustMatchers with BeforeAndAfter {
  val tokenizer = new StopWordsTokenizer


  "Sentence with two words with U+0096 in between" must "not include it as token but use it as delimiter" in {
    val sentence = "Foo\u0096bar"

    tokenizer.tokenize(Seq(sentence)) mustEqual Map("foo" -> 1, "bar" -> 1)
  }

  "Sentence with <a> html tags" must "be tokenized properly" in {
    val sentence = "Ambiguity is a type of <a href=\"uncertainty\">uncertainty</a> of <a href=\"meaning%20%28linguistics%29\">meaning</a> in which several interpretations are <a href=\"wikt%3Aplausible%23Adjective\">plausible</a>."
    val expected = Map(
      "ambiguity" -> 1,
      "type" -> 1,
      "uncertainty" -> 2,
      "meaning" -> 2,
      "linguistics" -> 1,
      "several" -> 1,
      "interpretations" -> 1,
      "plausible" -> 2,
      "adjective" -> 1)

    val actual = tokenizer.tokenize(Seq(sentence))

//    println(actual.toSet diff expected.toSet)

    actual mustEqual expected
  }
}
