package epam.idobrovolskiy.wikipedia.trending.cli

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
trait WikiQueryArgs {
  val queryType: WikiQueryType.Value
  val debug: Boolean = false

  override def toString: String = s"type=$queryType, debug=$debug"

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: WikiQueryArgs => canEqual(other) &&
      other.debug == this.debug &&
      other.queryType == this.queryType
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[WikiQueryArgs]
}
