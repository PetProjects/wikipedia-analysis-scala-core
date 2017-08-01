package epam.idobrovolskiy.wikipedia.trending.cli

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQueryType extends Enumeration {
  /**
    * Top N tokens, with highest occurrence rate within period are returned by the query of this type.
    * Occurrence rate is calculated based on all the Wikipedia documents which are considered related to any date within specified period.
    * */
  val TopTokensForPeriod = Value

  /**
    * Top N Wikipedia articles (links), with highest score function values within period should be returned by the query of this type.
    * Exact meaning of score function is still TBD, but from a priori point of view
    * it should be evaluated as weighted sum of article value itself and the relevance of the article to the specified period.
    * */
  val TopDocumentsForPeriod = Value

  /**
    * Based on specified token a distribution within all historical time frame should be returned by the query of this type.
    * It is still to be defined exact data structure of such distribution. But basically idea is to supply data which then could be used
    * to build a kind of bar chart for an exponentially scaled time axis.
    * */
  val PeriodDistributionForToken = Value
}
