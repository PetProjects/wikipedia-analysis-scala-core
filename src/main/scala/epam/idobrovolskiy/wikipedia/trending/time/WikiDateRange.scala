package epam.idobrovolskiy.wikipedia.trending.time


/**
  * Created by Igor_Dobrovolskiy on 04.08.2017.
  */
case class WikiDateRange
(
  @transient
  since: WikiDate,
  @transient
  until: WikiDate
)

