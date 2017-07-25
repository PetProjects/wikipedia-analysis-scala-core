package epam.idobrovolskiy.wikipedia.analysis.document

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
object ParseFailReason extends Enumeration {
  val Default, HeaderParsingFail, BodyParsingFail = Value
}
