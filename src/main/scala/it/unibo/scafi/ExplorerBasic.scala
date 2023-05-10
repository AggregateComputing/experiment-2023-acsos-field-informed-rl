package it.unibo.scafi

import it.unibo.learning.GaussianThreshold
import it.unibo.scafi.space.Point3D

class ExplorerBasic extends Agent {

  override def main(): Any = {
    val phenomena = senseEnvData[Double]("info")
    val source = maxHood(nbr(phenomena)) == phenomena && phenomena > GaussianThreshold.Value
    val direction = G[Point3D](source, Point3D.Zero, p => p + nbrVector(), nbrRange)
    val data = includingSelf.reifyField(nbr(currentPosition()), nbr(phenomena), nbrRange(), nbr(currentPosition()))
    node.put("hue", mid()) // for visualization
    node.put("direction", direction)
    node.put("directionArray", Array(direction.x, direction.y))
    node.put("phenomena", phenomena)
    node.put("source", source)
    node.put("constant", 1.0)
    // compute angle from direction
    node.put("center", math.atan2(-1 * direction.y, direction.x))
    val state = computeState(data)
    node.put("state", state)
    // node.put("action", policy(state))
  }

}
