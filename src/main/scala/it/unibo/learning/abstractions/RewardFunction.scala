package it.unibo.learning.abstractions

import it.unibo.learning.GaussianThreshold
import it.unibo.learning.abstractions.RewardFunction.{RewardInfo, RewardInput}

trait RewardFunction extends (RewardInput => RewardInfo) {}

object RewardFunction {
  type RewardInput = (AgentState, AgentState, Int, Double)
  type RewardInfo = (Double, Map[String, Double])

  def connectionAndInArea: RewardFunction = { case (_, currentState, _, _) =>
    val target = currentState.view * 2.0
    val mySelf = currentState.neighborhoodSensing.head(currentState.me)
    val targetReward = if(mySelf.data[Double] >  GaussianThreshold.Value) { 1 } else { 0 }
    val connectionReward = if (currentState.neighborhoodSensing.head.size < 3) 0 else 1
    val distances = currentState.
      neighborhoodSensing.head.withoutMe(currentState.me)
      .filter(_._2.data[Double] > GaussianThreshold.Value)
      .values.map(_.distance[Double])

    val minDistance = distances.minOption.getOrElse(0.0)
    val maxDistance = distances.maxOption.getOrElse(300.0)
    val deltaMax = maxDistance
    val deltaMin = minDistance

    def computeRegretFromDistance(distance: Double): Double = {
      if(distance > target) { 1.0 }
      else distance / target
    }
    val deltaMinFactor = 1 - computeRegretFromDistance(Math.abs(deltaMin - target))
    val deltaMaxFactor = 1 - computeRegretFromDistance(Math.abs(deltaMax - target))
    val collision = if(targetReward > 0) { deltaMinFactor } else { 0.0 } //if(targetReward > 0) { (deltaMinFactor + deltaMaxFactor) / 2 } else { 0.0 }
    val collisionFactor = if (targetReward > 0.0) { collision }
    else { 0.0 }

    val totalReward = ( connectionReward.inverse + collisionFactor.inverse) / 2.0
    (
      totalReward,
      Map(
        "target reward" -> targetReward.inverse,
        "connection reward" -> connectionReward.inverse,
        "collision reward" -> collisionFactor.inverse
      )
    )
  }

  implicit class InverseDouble(double: Double) {
    def inverse: Double = double - 1.0
  }
}
