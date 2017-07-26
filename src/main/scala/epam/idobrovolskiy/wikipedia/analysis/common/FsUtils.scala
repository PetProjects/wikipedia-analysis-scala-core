package epam.idobrovolskiy.wikipedia.analysis.common

import java.io.PrintWriter

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */
object FsUtils {
  def sinkToFile[T](fname: String, ss: Seq[T])(convert: T => String) = {
    val writer = new PrintWriter(fname)
    try
        for (v <- ss)
          writer.println(convert(v))

    finally
      writer.close()
  }
}
