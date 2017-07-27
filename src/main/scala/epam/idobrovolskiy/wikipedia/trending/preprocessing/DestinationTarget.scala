package epam.idobrovolskiy.wikipedia.trending.preprocessing

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object DestinationTarget extends Enumeration {
  val Stdout = Value(1)
  val LocalFs = Value(2)
  val StdoutAndLocalFs = Value(3)
  val HdfsPlainFile = Value(4)
  val StdoutAndHdfsPlainFile = Value(5)
  val HdfsSequenceFile = Value(8)
  val StdoutAndHdfsSequenceFile = Value(9)
}
