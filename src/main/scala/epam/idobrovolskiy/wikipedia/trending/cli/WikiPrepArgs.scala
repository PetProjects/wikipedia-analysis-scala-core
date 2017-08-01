package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.preprocessing.PreprocessingTarget

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */

case class WikiPrepArgs
(
  path: String,
  target: PreprocessingTarget.Value,
  fullText: Boolean,
  extractToPath: String,
  extractFromPath: String,
  extractPlainText: Boolean
)
