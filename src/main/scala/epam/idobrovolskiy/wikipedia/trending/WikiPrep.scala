package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.cli.{WikiPrepArgs, WikiPrepArgsParser}
import epam.idobrovolskiy.wikipedia.trending.preprocessing.WikiDocumentPreprocessor
import epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi._

object WikiPrep extends App {
  WikiPrepArgsParser.parse(args) match {
    case Some(args: WikiPrepArgs) =>
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
