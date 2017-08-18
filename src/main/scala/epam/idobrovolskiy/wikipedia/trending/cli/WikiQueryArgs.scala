package epam.idobrovolskiy.wikipedia.trending.cli

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
trait WikiQueryArgs {
  val queryType: WikiQueryType.Value
  val debug: Boolean
  val queryVersion: Int
  val useHive: Boolean

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

object WikiQueryArgs { //Rework all val-s to var-s?!

  def apply(qt: WikiQueryType.Value, d: Boolean = false, qv: Int = -1 /*latest*/ , uh: Boolean = false) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = qt
      override val debug: Boolean = d
      override val queryVersion: Int = qv
      override val useHive: Boolean = uh
    }

  def qType(qt: WikiQueryType.Value, args: WikiQueryArgs) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = qt
      override val debug: Boolean = args.debug
      override val queryVersion: Int = args.queryVersion
      override val useHive: Boolean = args.useHive
    }

  def debug(d: Boolean = true, args: WikiQueryArgs) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = args.queryType
      override val debug: Boolean = d
      override val queryVersion: Int = args.queryVersion
      override val useHive: Boolean = args.useHive
    }

  def version(qv: Int, args: WikiQueryArgs) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = args.queryType
      override val debug: Boolean = args.debug
      override val queryVersion: Int = qv
      override val useHive: Boolean = args.useHive
    }

  def hive(uh: Boolean = true, args: WikiQueryArgs) =
    new WikiQueryArgs {
      override val queryType: WikiQueryType.Value = args.queryType
      override val debug: Boolean = args.debug
      override val queryVersion: Int = args.queryVersion
      override val useHive: Boolean = uh
    }
}