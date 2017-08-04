package epam.idobrovolskiy.wikipedia.trending.time

import java.time.Month

import epam.idobrovolskiy.wikipedia.trending.tokenizer.StopWordsTokenizer
import org.scalatest.FunSuite

/**
  * Created by Igor_Dobrovolskiy on 02.08.2017.
  */
class WikiDateTest extends FunSuite {
  test("Test WikiDate after Ser-De results in source value") {
    val wd = WikiDate.AD(123, Month.AUGUST, 31)

    assertResult(wd) {
      import java.io._

      // Serialize
      val bo = new ByteArrayOutputStream
      val o = new ObjectOutputStream(bo)
      o.writeObject(wd)
      val bytes = bo.toByteArray

      // Deserialize
      val bi = new ByteArrayInputStream(bytes)
      val i = new ObjectInputStream(bi)
      val t = i.readObject.asInstanceOf[WikiDate]

      t
    }

    //Although the test passes, t holds incorrect representation field values (for year, month, day, etc.). Test should be updated after Ser-De issue is fixed in WikiDate class.
  }

  test("ser calc") {
    val date = WikiDate.deserialize(2233824) // 2129364 //2567655

    println(date)

    println("a-".split('-').mkString("[",",","]"))

    val t = new StopWordsTokenizer
//    val tt = t.getTopNTokens(Seq("""In Ireland, the rebel Irish Catholics formed their own government – Confederate Ireland with the intention of helping the Royalists in return for religious toleration and political autonomy."""))
    val tt = t.getTopNTokens(Seq("""government – Confederate """))
    println(tt)
  }
}
