package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.preprocessing.DestinationTarget

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */

case class WikiPrepArguments
(
  path: String,
  target: DestinationTarget.Value,
  fullText: Boolean,
  extractToPath: String,
  extractFromPath: String,
  extractPlainText: Boolean
)
