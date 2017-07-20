package epam.idobrovolskiy.wikipedia.analysis

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
class AttardiWikiDocumentTest extends FlatSpec with Matchers {
  val Id = 1
  val Url = "https://en.wikipedia.org/wiki?curid=1"
  val Title = "Some title"
  val Header = s"<doc id=${"\"" + Id + "\""} url=${"\"" + Url + "\""} title=${"\"" + Title + "\""}>"
  val HeaderWoId = s"<doc url=${"\"" + Url + "\""} title=${"\"" + Title + "\""}>"
  val HeaderWoTitle = s"<doc id=${"\"" + Id + "\""} url=${"\"" + Url + "\""} >"
  val HeaderWoUrl = s"<doc id=${"\"" + Id + "\""} title=${"\"" + Title + "\""}>"
  val Body = "Some text"
  val Body2 = "Some other text"
  val Ending = "</doc>"

  "Correct single document" should "be parsed into PlainWikiDocument with correct fields" in {
    val doc = AttardiWikiDocument(List(
      Header,
      Body,
      Ending
    ))

    doc should be(PlainWikiDocument(Id, Title, Url))
  }

  "Single document with multi line body containing blanks" should "be parsed correctly" in {
    val doc = AttardiWikiDocument(List(
      Header,
      Body,
      "",
      Body2,
      Ending
    ))

    doc should be(PlainWikiDocument(Id, Title, Url))
  }

  "Single document without ending" should "be parsed into NoWikiDocument with default fail reason" in {
    val doc = AttardiWikiDocument(List(
      Header,
      Body
    ))

    doc should be(NoWikiDocument(ParseFailReason.Default))
  }

  "Single document without id" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocument(List(
      HeaderWoId,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }

  "Single document without title" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocument(List(
      HeaderWoTitle,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }

  "Single document without url" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocument(List(
      HeaderWoUrl,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }
}
