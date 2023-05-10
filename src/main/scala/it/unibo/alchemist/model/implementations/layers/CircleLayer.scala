package it.unibo.alchemist.model.implementations.layers

import it.unibo.alchemist.model.interfaces.{Layer, Position2D}

// Similar to src/main/scala/it/unibo/alchemist/model/implementations/layers/SquareLayer.scala but for a circle
class CircleLayer[P <: Position2D[P]](
    val x: Double,
    val y: Double,
    val radius: Double,
    val valueBase: Double
) extends Layer[Double, P]
    with CenterBasedMovableLayer[P] {

  override def value(x: Double, y: Double): Double = valueBase
  override protected def isInside(x: Double, y: Double): Boolean =
    Math.sqrt(Math.pow(x - movingX, 2) + Math.pow(y - movingY, 2)) <= radius
}
