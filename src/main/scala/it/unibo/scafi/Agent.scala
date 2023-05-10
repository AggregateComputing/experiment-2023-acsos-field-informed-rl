package it.unibo.scafi

import it.unibo.alchemist.model.implementations.reactions.GlobalLearnerDecentralisedAgent
import it.unibo.alchemist.model.scafi.ScafiIncarnationForAlchemist._
import it.unibo.learning.abstractions.AgentState.NeighborInfo
import it.unibo.learning.abstractions.{AgentState, Contextual}

import scala.jdk.CollectionConverters.IteratorHasAsScala

trait Agent
    extends AggregateProgram
    with StandardSensors
    with ScafiAlchemistSupport
    with BlockG
    with BlockT
    with Gradients
    with StateManagement
    with FieldUtils {
  def computeState(data: Map[Int, (P, Double, Double, P)] = Map.empty): AgentState = {
    val neighborInfo = data
      .view
      .mapValues { case (information, density, distance, additionalInfo) => NeighborInfo(density, (information.x, information.y), distance, (additionalInfo.x, additionalInfo.y),  -1) }
      .toMap

    AgentState(mid(), node.get[Double]("view"), List(neighborInfo), Contextual.empty)
  }

  def policy: AgentState => Int = {
    alchemistEnvironment.getGlobalReactions
      .iterator()
      .asScala
      .collectFirst { case reaction: GlobalLearnerDecentralisedAgent[_, _] => reaction }
      .map(learning => learning.learner.policy)
      .getOrElse(_ => 0)
  }
}
