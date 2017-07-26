import epam.idobrovolskiy.wikipedia.analysis._

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */
case class WikiPrepArgumentsRaw(path: String, targetBitset: Int, fullText: Boolean)

object WikiPrepArgumentsRaw{
  def apply(): WikiPrepArgumentsRaw =
    new WikiPrepArgumentsRaw(path = DefaultInputFilePath, targetBitset = DefaultTarget.id, fullText = false)
  def apply(cp: WikiPrepArgumentsRaw, path: String): WikiPrepArgumentsRaw =
    new WikiPrepArgumentsRaw(path = path, targetBitset = cp.targetBitset, fullText = cp.fullText)
  def apply(cp: WikiPrepArgumentsRaw, targetBitset: Int): WikiPrepArgumentsRaw =
    new WikiPrepArgumentsRaw(targetBitset = targetBitset, path = cp.path, fullText = cp.fullText)
  def apply(cp: WikiPrepArgumentsRaw, fullText: Boolean): WikiPrepArgumentsRaw =
    new WikiPrepArgumentsRaw(fullText = fullText, targetBitset = cp.targetBitset, path = cp.path)
}

case class WikiPrepArguments(path: String, target: DestinationTarget.Value, fullText: Boolean)
