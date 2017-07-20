package epam.idobrovolskiy.wikipedia.analysis

import java.io.File

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */

class AttardiWikiDocumentProducer extends WikiDocumentProducer {

  /**
    * Main entry point for parsing both directories and file.
    **/
  override def getDocuments(path: String): Stream[WikiDocument] = {
    require(path.nonEmpty && new File(path).exists(), "Invalid path")

    val file = new File(path)
    if (file.isFile)
      getDocumentsFromFile(path)
    else
      getDocumentsFromDirectory(file)
  }

  /**
    * Recursively process current and inner directories for files. Each file parser is concatenated into resulting stream.
    **/
  private def getDocumentsFromDirectory(directory: File): Stream[WikiDocument] = {
    val files = directory.listFiles()

    val innerFilesStream = files.filter(_.isDirectory)
      .map(f => getDocumentsFromDirectory(f))
      .foldLeft(Stream.empty[WikiDocument])(_ #::: _)

    files.filter(_.isFile)
      .map(f => getDocumentsFromFile(f.getAbsolutePath))
      .foldLeft(innerFilesStream)(_ #::: _)
  }

  /**
    * Main entry point for parsing single file.
    **/
  private def getDocumentsFromFile(filePath: String): Stream[WikiDocument] =
    getDocuments(Source.fromFile(filePath).getLines(), Stream.empty)

  private def getDocuments(fileLines: Iterator[String], cStream: Stream[WikiDocument]): Stream[WikiDocument] =
    if (!fileLines.hasNext)
      cStream
    else
      parseDocument(fileLines) #:: getDocuments(fileLines, cStream)

  /**
    * Main entry point for parsing single wiki document.
    **/
  private def parseDocument(fileLines: Iterator[String]): WikiDocument =
    getFileLines(fileLines.buffered, _.contains("</doc>")) match {
      case Some(lines) => AttardiWikiDocument(lines)
      case None => NoWikiDocument(ParseFailReason.Default)
    }

  /**
    * Processes buffered iterator, until predicate of end of document occurs.
    * Returns an option of list of document lines or None in case of no document ending mark found.
    */
  private def getFileLines(fileLines: BufferedIterator[String], endOfDoc: String => Boolean): Option[List[String]] = {
    val lines = ListBuffer.empty[String]
    while (fileLines.hasNext && !endOfDoc(fileLines.head)) {
      lines += fileLines.head
      fileLines.next()
    }

    fileLines.headOption match {
      case Some(s) => lines += s; Some(lines.toList)
      case _ => None
    }
  }
}
