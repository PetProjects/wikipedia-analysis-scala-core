package epam.idobrovolskiy.wikipedia.trending.cli

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
trait WikiQueryArgs {
  val queryType: WikiQueryType.Value
  val debug: Boolean
  val queryVersion: Int

  override def toString: String = s"type=$queryType, debug=$debug, queryVersion=$queryVersion"

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: WikiQueryArgs => canEqual(other) &&
      other.debug == this.debug &&
      other.queryType == this.queryType &&
      other.queryVersion == this.queryVersion
  }

  override def hashCode(): Int = (queryType, debug, queryVersion).##

  def canEqual(other: Any): Boolean = other.isInstanceOf[WikiQueryArgs]
}

object WikiQueryArgs {
  def apply(qt: WikiQueryType.Value, d: Boolean = false, qv: Int = -1 /*latest*/) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = qt
      override val debug: Boolean = d
      override val queryVersion: Int = qv
    }
}