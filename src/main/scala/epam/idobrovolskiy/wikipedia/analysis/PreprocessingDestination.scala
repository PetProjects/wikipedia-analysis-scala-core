package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object PreprocessingDestination extends Enumeration {
  val Stdout = Value(1)
  val LocalFs = Value(2)
  val StdoutAndLocalFs = Value(3)
  val Hdfs = Value(4)
  val StdoutAndHdfs = Value(5)
}
