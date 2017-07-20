package epam.idobrovolskiy.wikipedia.analysis

import java.io.File

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */

class AttardiWikiDocumentProducer extends WikiDocumentProducer {


  /** Main entry point
    * */
  override def getDocuments(path: String): Stream[WikiDocument] = {
    require(path.nonEmpty && new File(path).exists(), "Invalid path")

    val file = new File(path)
    if (file.isFile)
      getDocumentsFromFile(path)
    else
      getDocumentsFromDirectory(file)
  }

  private def getDocumentsFromDirectory(directory: File): Stream[WikiDocument] = {
    val files = directory.listFiles()

    val innerFilesStream = files.filter(_.isDirectory)
      .map(f => getDocumentsFromDirectory(f))
      .foldLeft(Stream.empty[WikiDocument])(_ #::: _)

    files.filter(_.isFile)
      .map(f => getDocumentsFromFile(f.getAbsolutePath))
      .foldLeft(innerFilesStream)(_ #::: _)
  }

  private def getDocumentsFromFile(filePath: String): Stream[WikiDocument] =
    getDocuments(Source.fromFile(filePath).getLines(), Stream.empty)

  private def getDocuments(fileLines: Iterator[String], cStream: Stream[WikiDocument]): Stream[WikiDocument] =
    if(!fileLines.hasNext)
      cStream
    else
      parseDocument(fileLines) #:: getDocuments(fileLines, cStream)

  private def parseDocument(fileLines: Iterator[String]): WikiDocument =
    getFileLines(fileLines.buffered) match {
      case Some(lines) => AttardiWikiDocument(lines)
      case None => NoWikiDocument(ParseFailReason.Default)
    }

  private def getFileLines(fileLines: BufferedIterator[String]): Option[List[String]] = {
    val lines = ListBuffer.empty[String]
    while (fileLines.hasNext && !fileLines.head.contains("</doc>")) {
      lines += fileLines.head
      fileLines.next()
    }

    fileLines.headOption match {
      case Some(s) => lines += s; Some(lines.toList)
      case _ => None
    }
  }
}
