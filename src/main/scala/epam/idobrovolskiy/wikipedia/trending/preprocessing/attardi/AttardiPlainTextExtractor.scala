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

  //TODO: remove set and related logics after completing whole wiki run
  val filesToSkip = Set(
    "p10p30302",
    "p11018059p11539266",
    "p11539268p13039268",
    "p13039268p13693066",
    "p13693075p15193075",
    "p15193075p16120541",
    "p16120548p17620548",
    "p2336425p3046511",
    "p3046517p3926861",
    "p3926864p5040435",
    "p5040438p6197593",
    "p6197599p7697599",
    "p7697599p7744799",
    "p7744803p9244803",
    "p9244803p9518046",
    "p9518059p11018059",
    "p17620548p18754723","p18754736p20254736","p20254736p21222156","p21222161p22722161","p22722161p23927980","p23927984p25427984","p30304p88444"
  )

  def extract(inPath: String, outPath: String): Unit = {
    val inFiles = getInFiles(inPath)

    import scala.sys.process._

    for (inFile <- inFiles) {
      val cInPath = inFile.getPath

      val (cOutPath: String, suffix: String) = cInPath match {
        case SuffixRe(suffix) => (outPath + "/" + suffix, suffix)
        case _ => outPath
      }

      if(! filesToSkip.contains(suffix)) {
        val cCmdLine = cmdLine(cInPath, cOutPath)
        if ((cCmdLine !) != 0)
          throw new RuntimeException(s"Plain text extraction failed for file: ${inFile.getAbsolutePath}")
      }
    }
  }
}
