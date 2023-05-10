package it.unibo.learning.abstractions

import it.unibo.learning.GaussianThreshold
import it.unibo.learning.abstractions.AgentState.NeighborInfo

import scala.collection.immutable.Queue
import scala.language.dynamics

/** */
case class AgentState(
    me: Int,
    view: Double,
    neighborhoodSensing: List[Map[Int, NeighborInfo]],
    contextual: Contextual
) {
  def extractCurrentLocal: NeighborInfo = neighborhoodSensing.head(me)
}

object AgentState {
  object NeighborInfo {
    def apply(data: Double, information: (Double, Double), neighborhoodDistance: Double, additionalInfo: (Double, Double), oldAction: Int): NeighborInfo =
      new NeighborInfo(
        Map(
          "data" -> data, //(if(data > GaussianThreshold.Value) { 1.0 } else { 0.0 }),
          "information" -> information,
          "distance" -> neighborhoodDistance,
          "additionalInfo" -> additionalInfo,
          "oldAction" -> oldAction
        )
      )
  }
  class NeighborInfo(map: Map[String, Any]) extends scala.Dynamic {
    def selectDynamic[A](name: String): A = map(name).asInstanceOf[A]

    override def toString: String = s"information: ${map.toString()}"
  }

  implicit class MapExtension(map: Map[String, Any]) {
    def obtain[S](key: String): S = map(key).asInstanceOf[S]
  }

  implicit class MapNeighExtension(map: Map[Int, NeighborInfo]) {
    def withoutMe(me: Int): Map[Int, NeighborInfo] = map.filterNot(_._1 == me)
    def me(me: Int): NeighborInfo = map(me)
  }
}
