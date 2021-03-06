package epam.idobrovolskiy.wikipedia.trending.attardi

import epam.idobrovolskiy.wikipedia.trending.document.{NoWikiDocument, ParseFailReason, WikiDocumentHeader}
import epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi.AttardiWikiDocumentParser
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
class AttardiWikiDocumentParserTest extends FlatSpec with Matchers {
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
    val doc = AttardiWikiDocumentParser.parseHeader(IndexedSeq(
      Header,
      Body,
      Ending
    ))

    doc should be(WikiDocumentHeader(Id, Title, Url))
  }

  "Single document with multi line body containing blanks" should "be parsed correctly" in {
    val doc = AttardiWikiDocumentParser.parseHeader(IndexedSeq(
      Header,
      Body,
      "",
      Body2,
      Ending
    ))

    doc should be(WikiDocumentHeader(Id, Title, Url))
  }

  "Single document without ending" should "be parsed into NoWikiDocument with default fail reason" in {
    val doc = AttardiWikiDocumentParser.parseBasicStats(IndexedSeq(
      Header,
      Body
    ))

    doc should be(NoWikiDocument(ParseFailReason.BodyParsingFail))
  }

  "Single document without id" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocumentParser.parseHeader(IndexedSeq(
      HeaderWoId,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }

  "Single document without title" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocumentParser.parseHeader(IndexedSeq(
      HeaderWoTitle,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }

  "Single document without url" should "be parsed into NoWikiDocument with 'header parsing fail' reason" in {
    val doc = AttardiWikiDocumentParser.parseHeader(IndexedSeq(
      HeaderWoUrl,
      Body,
      Ending
    ))

    doc should be(NoWikiDocument(ParseFailReason.HeaderParsingFail))
  }
}
