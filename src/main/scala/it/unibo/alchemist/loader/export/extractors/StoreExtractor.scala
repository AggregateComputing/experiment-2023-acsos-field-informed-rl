package it.unibo.alchemist.loader.`export`.extractors

import it.unibo.alchemist.Store
import it.unibo.alchemist.loader.`export`.Extractor
import it.unibo.alchemist.model.interfaces.{Actionable, Environment, Time}

import java.util
import scala.jdk.CollectionConverters.MapHasAsJava

class StoreExtractor extends Extractor[Double] {
  override def getColumnNames: util.List[String] =
    util.List.of[String](Store.values.keys.toSeq:_*)

  override def extractData[T](environment: Environment[T, _], actionable: Actionable[T], time: Time, l: Long): util.Map[String, Double] = {
    val values = Store.values
    values.asJava
  }
}
