package it.unibo.alchemist.model.implementations.layers

import it.unibo.alchemist.model.interfaces.Position2D
import it.unibo.alchemist.model.math.BidimensionalGaussian

class BidimensionalMovableLayer[P <: Position2D[P]](val x: Double, val y: Double, val norm: Double, val sigma: Double) extends CenterBasedMovableLayer[P] {
  println((x, y, norm, sigma))
  def gaussianLayer = new BidimensionalGaussian(norm, movingX, movingY, sigma, sigma)

  override def value(x: Double, y: Double): Double =
    gaussianLayer.value(x, y)
  override protected def isInside(x: Double, y: Double): Boolean = true
}

