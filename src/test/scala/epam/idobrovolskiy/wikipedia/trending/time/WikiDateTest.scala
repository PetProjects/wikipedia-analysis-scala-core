package epam.idobrovolskiy.wikipedia.trending.time

import java.time.Month

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

  test("NoDate equals NoDate") {
    assertResult(WikiDate.NoDate) {
      WikiDate.NoDate
    }
  }

  test("AD(1, January, 1) not equals NoDate") {
    assert(WikiDate.AD(1, Month.JANUARY, 1) != WikiDate.NoDate)
  }

  test("BC(1, December, 31) not equals NoDate") {
    assert(WikiDate.BC(1, Month.DECEMBER, 31) != WikiDate.NoDate)
  }

  test("AD(1, January, 1).serialize not equals NoDate.serialize") {
    assert(WikiDate.AD(1, Month.JANUARY, 1).serialize != WikiDate.NoDate.serialize)
  }

  test("BC(1, December, 31).serialize not equals NoDate.serialize") {
    assert(WikiDate.BC(1, Month.DECEMBER, 31).serialize != WikiDate.NoDate.serialize)
  }

  test("<entrypoint for fast runtime runs/checks (not a test)>") {
    val date = WikiDate.deserialize(2233824) // 2129364 //2567655

//    println(date)
//    println(WikiDate.NoDate.toClosestTraditionalDate, WikiDate.AD(1).toClosestTraditionalDate, WikiDate.BC(1, Month.DECEMBER, 31).toClosestTraditionalDate)
//    println(WikiDate.BC(1, Month.DECEMBER, 31).serialize, WikiDate.NoDate.serialize, WikiDate.AD(1).serialize)

    println(WikiDate.BC(50).serialize, WikiDate.AD(50).serialize)
  }
}
