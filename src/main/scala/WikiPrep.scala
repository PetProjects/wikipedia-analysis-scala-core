import epam.idobrovolskiy.wikipedia.analysis._

object WikiPrep extends App {
  WikiPrepArgumentsParser.parse(args) match {
    case Some(args: WikiPrepArguments) =>
      if(!args.fullText)
        WikiDocumentPreprocessor.preprocessStats(args.path, args.target)
      else
        WikiDocumentPreprocessor.preprocess(args.path,
          args.target,
          "wiki_full",
          serializeOnlyStats = false,
          new attardi.AttardiWikiDocumentProducer(attardi.AttardiWikiDocumentParsingStrategy.ToFullText))

    case _ =>
  }
}
