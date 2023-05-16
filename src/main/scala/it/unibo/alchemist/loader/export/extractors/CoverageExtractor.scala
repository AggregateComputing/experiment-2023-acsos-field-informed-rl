package it.unibo.alchemist.loader.`export`.extractors

import it.unibo.alchemist.loader.`export`.Extractor
import it.unibo.alchemist.model.implementations.layers.CircleLayer
import it.unibo.alchemist.model.interfaces
import it.unibo.alchemist.model.interfaces.{Actionable, Environment, Node, Position}

import java.awt.Color
import java.awt.geom._
import java.awt.image.BufferedImage
import java.io.File
import java.util
import javax.imageio.ImageIO
import scala.jdk.CollectionConverters.IteratorHasAsScala

class CoverageExtractor(val size: Double, layers: Int) extends Extractor[Double] {

  def this(size: Double) =
    this(size, 1)
  override def getColumnNames: util.List[String] = {
    if(layers > 1)
      util.List.of("coverage", "coverage_1", "coverage_2")
    else
      util.List.of("coverage")
  }

  override def extractData[T](
      environment: Environment[T, _],
      actionable: Actionable[T],
      time: interfaces.Time,
      l: Long
  ): util.Map[String, Double] = {
    val nodes = environment.getNodes.iterator().asScala.map(node => environment.typedPosition(node))
    val coordinates = nodes.map(position => (position.getCoordinate(0), position.getCoordinate(1)))
    val shapes = coordinates.map { case (x, y) => new Ellipse2D.Double(x - size / 2, -(y - size / 2), size, size) }
    val layers = environment.getLayers
      .iterator()
      .asScala
      .filter(_.isInstanceOf[CircleLayer[_]])
      .map(_.asInstanceOf[CircleLayer[_]])
      .toList
    val circles = layers
      .map(layer =>
        new Ellipse2D.Double(layer.x - layer.radius, -layer.y - layer.radius, layer.radius * 2, layer.radius * 2)
      )

    def computeCoverage(reference: Ellipse2D): Double = {
      val path = new Path2D.Double()
      val merged = shapes.foldLeft[Path2D.Double](path) { (acc, area) => acc.append(area, false); acc }
      val area = new Area(merged)
      val referenceArea = new Area(reference)
      val totalArea = referenceArea.area
      referenceArea.subtract(area)
      val covered = referenceArea.area
      (totalArea - covered) / totalArea
    }
    val partialCoverage = circles.map(computeCoverage)
    val totalCoverage = partialCoverage.sum / circles.size
    if(this.layers > 1)
      util.Map.of("coverage", totalCoverage, "coverage_1", partialCoverage.head, "coverage_2", partialCoverage(1))
    else
      util.Map.of("coverage", totalCoverage)
  }

  implicit class RichEnvironment[T](environment: Environment[T, _]) {
    def typedPosition(node: Node[T]): Position[_] =
      environment.getPosition(node).asInstanceOf[Position[_]]
  }

  implicit class RichArea(self: Area) {
    def area: Double = {
      var xBegin, yBegin, yPrev, xPrev, sum = 0f
      val iterator = self.getPathIterator(null, 0.1)
      val coords = Array.ofDim[Float](6)
      while (!iterator.isDone) {
        val element = iterator.currentSegment(coords)
        element match {
          case PathIterator.SEG_MOVETO =>
            xBegin = coords(0)
            yBegin = coords(1)

          case PathIterator.SEG_LINETO =>
            sum += (coords(0) - xPrev) * (coords(1) + yPrev) / 2.0f

          case PathIterator.SEG_CLOSE =>
            sum += (xBegin - xPrev) * (yBegin + yPrev) / 2.0f
        }
        xPrev = coords(0)
        yPrev = coords(1)
        iterator.next()
      }
      sum
    }
  }
}
