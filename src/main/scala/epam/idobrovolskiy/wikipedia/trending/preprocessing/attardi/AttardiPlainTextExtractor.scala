package epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi

import java.io.{File, FilenameFilter}

import epam.idobrovolskiy.wikipedia.trending.preprocessing.PlainTextExtractor

/**
  * Created by Igor_Dobrovolskiy on 27.07.2017.
  */
object AttardiPlainTextExtractor extends PlainTextExtractor {
  val AttardiLibPath = "./lib/attardi/wikiextractor/"

  private def getInFiles(inPath: String) = {
    val inDir = new File(inPath)
    if (inDir.isDirectory)
      inDir.listFiles(new FilenameFilter {
        def accept(dir: File, name: String) = name.endsWith(".bz2")
      })
    else
      Array(inDir)
  }

  private def cmdLine(inPath: String, outPath: String): String =
    s"python ${AttardiLibPath}WikiExtractor.py -o $outPath  --bytes 100M --processes 3 --links --sections --lists $inPath"

  private val SuffixRe = """.+xml\-(\w+)\.bz2""".r

  def extract(inPath: String, outPath: String): Unit = {
    val inFiles = getInFiles(inPath)

    import scala.sys.process._

    for (inFile <- inFiles) {
      val cInPath = inFile.getPath

      val cOutPath = cInPath match {
        case SuffixRe(suffix) => outPath + "/" + suffix
        case _ => outPath
      }

      val cCmdLine = cmdLine(cInPath, cOutPath)
      if ((cCmdLine !) != 0)
        throw new RuntimeException(s"Plain text extraction failed for file: ${inFile.getAbsolutePath}")
    }
  }
}
