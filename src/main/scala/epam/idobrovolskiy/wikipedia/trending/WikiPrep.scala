package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.preprocessing.WikiDocumentPreprocessor
import epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi._

object WikiPrep extends App {
  WikiPrepArgumentsParser.parse(args) match {
    case Some(args: WikiPrepArguments) =>
      if(!args.fullText)
        WikiDocumentPreprocessor.preprocessStats(args,
          DefaultOutputFilename)
      else
        WikiDocumentPreprocessor.preprocess(args,
          DefaultFullTextOutputFilename,
          new AttardiWikiDocumentProducer(AttardiWikiDocumentParsingStrategy.ToFullText))

    case _ =>
  }
}
